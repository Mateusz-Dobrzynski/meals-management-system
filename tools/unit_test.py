import mysql.connector
cnx = mysql.connector.connect(host='127.0.0.1', database='mms_database',user='root', port=3306)

mycursor = cnx.cursor()

sql = "INSERT INTO users (email, password) VALUES (%s, %s)"
val = ("anna@something.us", "hkjh")
mycursor.execute(sql, val)

cnx.commit()
