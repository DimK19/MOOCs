import json
import urllib.request
import os
import re
import sqlite3

import faker
import random

fake = faker.Faker()

con = sqlite3.connect("C:\\Users\\KYRIAKOS\\Desktop\\Flask_Course_Project\\src\\flaskMoviesApp\\flask_movies_database.db")
cur = con.cursor()

with open("names.json", 'r') as n:
    names = json.loads(n.readline())

f = open("movies.json", 'r')
i = 0

for line in f:
    i += 1
    data = json.loads(line)
    imgname = re.sub(r"\W", "", data["title"])
    rdt = fake.date_time_between(start_date = "-20d")
    uid = random.randint(1, 5)
    query = 'INSERT INTO `movie` (id, title, plot, image, rating, release_year, date_created, user_id) VALUES ({}, "{}", "{}", "{}", {}, {}, "{}", {});'.format(i, data["title"], data["plot"].replace('\"', ''), names[imgname], data["rating"], data["release_year"], rdt, uid)
    cur.execute(query)
    cur.execute("COMMIT;")

f.close()
con.close()
