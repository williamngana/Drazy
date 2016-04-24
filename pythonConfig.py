import MySQLdb
import json

class PyMySQL(object):

	connection = MySQLdb.connect("52.165.45.75","root", "Hack123","drazy" )
	def db_query(self, query):
		cursor = self.connection.cursor()
		result = cursor.execute(query)
		if result == False:
			return json.dumps({'sucess':0, 'message': 'there was an error'})
		else:
			return json.dumps({'sucess':1, 'message': 'sucessfully completed query'})
		self.connection.close()

	def db_select(self, query):
		cursor = self.connection.cursor()
		cursor.execute(query)
		result = cursor.fetchall()
		if result == False:
			return json.dumps({'sucess':0, 'message': 'there was an error'})
		else:
			print result

class UserTable(object):
	user = PyMySQL()

	def get(self, Id, field):
		result = user.db_select("Select %s from appUser where P_Id = %s" %(field,Id))

	def post(self, username, firstname, lastname, sex, activityscore, password):
		user.db_query("INSERT INTO appUser (username,firstname,lastname,sex,dateCreated,activityscore,password) VALUES (%s,%s,%s,%s,now(),%s,%s);" %(username, firstname, lastname, sex, activityscore,password))

	def update(self, username, firstname, lastname, sex,password,Id):
		user.db_query("UPDATE appUser SET username = %s, irstname=%s, lastname=%s, activityscore=%s, password=%s WHERE P_Id =%s" %(username, firstname,lastname, sex, password))


class TagsTable(object):
	user = PyMySQL()

	def get(self, Id, field):
		result = user.db_select("Select %s from Tags where P_Id = %s" %(field,Id))

	def post(self, tags, Id):
		user.db_query("INSERT INTO Tags (tags, P_Id) VALUES (%s,%s);" %(tags,Id))

	def delete(self, tags):
		user.db_query("DELETE FROM Tags (tags) WHERE (%s);" %(tags))
		

class FriendsTable(object):
	user = PyMySQL()

	def get(self, Id):
		result = user.db_select("Select UserID2 from Friends where UserID1 = %s" %(Id))


class AirportsTable(object):
	user = PyMySQL()

	def get(self,Country):
		result = user.db_select("Select * from Airports where Country = %s" %(Country))



		






	


		



	
