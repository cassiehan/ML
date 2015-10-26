from guineapig import *
import re, string
import sys
import math

# supporting routines can go here
table = string.maketrans("","")
def train_tokens(train_lines):
	m = re.match(r"(\d+)\t(?P<label_group>\S+)\t(?P<word_group>.+)", train_lines)
	labels = m.group('label_group')
	words = m.group('word_group')
	words = words.translate(table, string.punctuation)
	for label in labels.split(","):
		for word in words.split(" "):
			word = word.translate(table, string.punctuation)
			if word != "":
				yield word, label
				yield "w*", label
		yield "", label
		yield "","y*"


# def train_tokens(train_lines):
# 	index1 = train_lines.index("\t") + 1
# 	line1 = train_lines[index1:]
# 	index2 = line1.index("\t")
# 	labels = line1[:index2]
# 	words = line1[index2+1:-1]
# 	for label in labels.split(","):
# 		for word in words.split(" "):
# 			word = word.translate(table, string.punctuation)
# 			if word != "":
# 				yield word, label
# 		yield "", label
# 		yield "", "y*"

def test_tokens(test_lines):
	m = re.match(r"(?P<id_group>\d+)\t(?P<label_group>\S+)\t(?P<word_group>.+)", test_lines)
	idnum = m.group('id_group')
	words = m.group('word_group')
	words = words.translate(table, string.punctuation)
	for word in words.split(" "):
		word = word.translate(table, string.punctuation)
		if word != "":
			yield idnum, word

# def test_tokens(test_lines):
# 	index1 = test_lines.index("\t") + 1
# 	idnum = test_lines[:index1-1]
# 	line1 = test_lines[index1:]
# 	index2 = line1.index("\t")
# 	words = line1[index2+1:-2]
# 	for word in words.split(" "):
# 		word = word.translate(table, string.punctuation)
# 		if word != "":
# 			yield idnum, word


def getProbability(line):
	docid = line[0]
	wordsize = line[1]
	labelsize = line[2]
	wordlist = line[3]
	dict_first = {}
	dict_second = {}
	for word in wordlist:
		eventlist = word[1]
		for event in eventlist:
			label = event[0]
			event_count = event[1]
			label_count = min((event[2])[0],(event[2])[1])
			label_w = max((event[2])[0],(event[2])[1])
			if dict_first.has_key(event[0]):
				dict_first[label] = dict_first[label] + math.log((event_count+1.0)/(label_w+1.0/(wordsize-1)))
			else:
				dict_first[event[0]] = math.log((event_count+1.0)/(label_w+1.0/(wordsize-1)))
				dict_second[event[0]] = math.log((label_count+1.0)/(labelsize+1.0/20.0))
	label = dict_first.keys()
	first = dict_first.values()
	second = dict_second.values()
	probability = []
	i=0
	while(i<len(label)):
		total = first[i] + second[i]
		probability.append(total)
		i = i + 1
	max_weight = min(probability)
	index = probability.index(max_weight)
	return docid, label[index], max_weight


#always subclass Planner
class NB(Planner):
	# params is a dictionary of params given on the command line. 
	# e.g. trainFile = params['trainFile']
	params = GPig.getArgvParams()

	train_wc = ReadLines(params['trainFile'])| Flatten(by=train_tokens) | Group(by=lambda x:x, reducingTo=ReduceToCount())

	label = Filter(train_wc, by=lambda ((word, label), count): (word=="" or word=="w*") and label!="y*") \
	| ReplaceEach(by=lambda ((word, label), count):(label, count)) \
	| Group(by=lambda (label, count):label, retaining=lambda (label, count):count, reducingTo=ReduceToList())

	label_size = Filter(train_wc, by=lambda ((word, label), count): label=="y*") 
	
	from_train = Join( Jin(train_wc, by=lambda ((word, label), event_count):label), Jin(label, by=lambda (label, label_list):label))\
	| ReplaceEach(by=lambda (((word, label1), event_count),(label, label_list)):(word, (label1, event_count, label_list))) \
	| Group(by=lambda (word, (label, event_count, label_list)):word, retaining=lambda (word, (label, event_count, label_list)):(label, event_count, label_list), reducingTo=ReduceToList()) 

	word_size = Group(from_train, by=lambda (word, eventlist): "ANY", reducingTo=ReduceTo(int, lambda accum,(word, eventlist): accum+1))

	from_test = ReadLines(params['testFile']) | Flatten(by=test_tokens)

	comp = Join( Jin(from_train, by=lambda (word, eventlist):word), Jin(from_test, by=lambda (idnum, word):word)) \
	| ReplaceEach(by=lambda ((word1, eventlist), (idnum, word2)):(idnum, (word1, eventlist)))  \
	| Group(by=lambda (idnum, (word1, eventlist)):idnum, retaining=lambda (idnum, (word1, eventlist)):(word1, eventlist), reducingTo=ReduceToList())

	comp2 = Join( Jin(comp, by=lambda (idnum, idlist):"ANY"), Jin(word_size, by=lambda (any, wordsize):"ANY"))

	comp3 = Join( Jin(comp2, by=lambda ((idnum, idlist), (any, wordsize)):"ANY"), Jin(label_size, by=lambda ((word,label), labelsize):"ANY")) \
	| ReplaceEach(by=lambda (((idnum, idlist), (any, wordsize)), ((word,label), labelsize)):(idnum, wordsize, labelsize, idlist))

	output = ReplaceEach(comp3, by=getProbability)


# always end like this
if __name__ == "__main__":
    NB().main(sys.argv)

# supporting routines can go here
