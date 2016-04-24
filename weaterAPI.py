from forecastiopy import *

appkey = '7ad4fe4ad7fc6b04e89408875f26ec57'

Location = [38.7252993, -9.1500364]

def getCurrentWeather():
	fio = ForecastIO.ForecastIO(appkey, latitude=Location[0], longitude=Location[1])
	#current = FIOCurrently.FIOCurrently(fio)
	#print fio.get_url() 

	if fio.has_currently() is True:
		currently = FIOCurrently.FIOCurrently(fio)
		print 'Currently'

		for item in currently.get().keys():
			print item + ' : ' + unicode(currently.get()[item])
	else:
		print 'No Currently data'	




