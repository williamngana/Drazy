import MySQLdb

raw_data = [line.strip() for line in open('airports_modified.txt', 'r')];
data = []
sqlData = []
f = open('sqlData.txt', 'w')
for i in raw_data:
	data += [[x.strip() for x in i.split(',')]]
for j in range(len(data)):
	sqlData += [[data[j][1],data[j][2],data[j][3],
	     		data[j][6],data[j][7]]]
for k in range(len(sqlData)):
	for l in sqlData[k]:
		f.write(l + "\t")
f.close


#Open database connection
db = MySQLdb.connect("localhost",   #host name 
	                 "root",   # username 
	                 "password",    #passoword
	                 "drazey" )   #name of database

#prepare a cursor object using cursor() method
cursor = db.cursor()
cursor.execute("LOAD DATA INFILE '/sqlData.txt' INTO TABLE Airports;") 

db.close()