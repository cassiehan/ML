import re, string, math

def getProbability(line):
	docid = line[0]
	wordsize = line[1]
	print(1, wordsize)
	labelsize = line[2]
	print(2, labelsize)
	wordlist = line[3]
	print(3, wordlist)
	dict_first = {}
	dict_second = {}
	for word in wordlist:
		eventlist = word[1]
		print(4, eventlist)
		for event in eventlist:
			print(5, event)
			if dict_first.has_key(event[0]):
				print(6, event[0],event[1],event[2], math.log((event[1]+1.0)/(event[2]+1.0/(wordsize-1))))
				dict_first[event[0]] = dict_first[event[0]] + math.log((event[1]+1.0)/(event[2]+1.0/(wordsize-1)))
			else:
				print(7,event[0],event[1],event[2], math.log((event[1]+1.0)/(event[2]+1.0/(wordsize-1))), math.log((event[2]+1.0)/(labelsize+1.0/20.0)))
				dict_first[event[0]] = math.log((event[1]+1.0)/(event[2]+1.0/(wordsize-1)))
				dict_second[event[0]] = math.log((event[2]+1.0)/(labelsize+1.0/20.0))
	label = dict_first.keys()
	print(8, label)
	first = dict_first.values()
	print(9, first)
	second = dict_second.values()
	print(10, second)
	probability = []
	i=0
	while(i<len(label)):
		total = first[i] + second[i]
		probability.append(total)
		print(11, label[i], first[i], second[i], total, probability[i])
		i = i + 1
	max_weight = max(probability)
	index = probability.index(max_weight)
	return docid, label[index], max_weight

train_line="33"+"\t"+"a,d,v"+"\t"+"d f d s"
# print(getProbability(('157', 11, 9, [('Abd', [('Agent', 1, 1), ('Person', 1, 1), ('Work', 2, 2)]), ('Abd', [('Agent', 1, 1), ('Person', 1, 1), ('Work', 2, 2)])])))
print(getProbability(
	('2556088', 430, 59, [
		('River', [('Location', 6, 14), ('NaturalPlace', 5, 3), ('Other', 5, 3), ('Place', 6, 14), ('PopulatedPlace', 1, 10)]), 
		('Romania', [('Location', 5, 14), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Place', 5, 14), ('PopulatedPlace', 2, 10)]), 
		('Sebes', [('Location', 1, 14), ('NaturalPlace', 1, 3), ('Other', 1, 3), ('Place', 1, 14)]), 
		('The', [('Location', 5, 14), ('MusicalWork', 1, 1), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Place', 5, 14), ('PopulatedPlace', 1, 10), ('Work', 1, 1)]), 
		('Tiganu', [('Location', 1, 14), ('NaturalPlace', 1, 3), ('Other', 1, 3), ('Place', 1, 14)]), 
		('a', [('Agent', 10, 4), ('Athlete', 8, 2), ('Location', 18, 14), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Person', 9, 3), ('Place', 18, 14), ('PopulatedPlace', 14, 10), ('Settlement', 5, 4)]), 
		('headwater', [('Location', 1, 14), ('NaturalPlace', 1, 3), ('Other', 1, 3), ('Place', 1, 14)]), 
		('in', [('Agent', 18, 4), ('Athlete', 14, 2), ('Location', 18, 14), ('MusicalWork', 2, 1), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Person', 15, 3), ('Place', 18, 14), ('PopulatedPlace', 14, 10), ('Settlement', 8, 4), ('Work', 2, 1)]), 
		('is', [('Agent', 4, 4), ('Athlete', 2, 2), ('Location', 17, 14), ('MusicalWork', 2, 1), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Person', 3, 3), ('Place', 17, 14), ('PopulatedPlace', 13, 10), ('Settlement', 4, 4), ('Work', 2, 1)]), 
		('of', [('Agent', 6, 4), ('Athlete', 6, 2), ('Location', 28, 14), ('MusicalWork', 1, 1), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Person', 6, 3), ('Place', 28, 14), ('PopulatedPlace', 21, 10), ('Settlement', 9, 4), ('Work', 1, 1)]), 
		('river', [('Location', 3, 14), ('NaturalPlace', 1, 3), ('Other', 1, 3), ('Place', 3, 14), ('PopulatedPlace', 2, 10)]), 
		('the', [('Agent', 13, 4), ('Athlete', 13, 2), ('Location', 29, 14), ('MusicalWork', 6, 1), ('NaturalPlace', 3, 3), ('Other', 3, 3), ('Person', 13, 3), ('Place', 29, 14), ('PopulatedPlace', 19, 10), ('Settlement', 4, 4), ('Work', 6, 1)])])))
