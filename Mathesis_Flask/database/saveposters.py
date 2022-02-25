import os
import urllib.request
import secrets
import json
import re

path = os.path.abspath("../src/flaskMoviesApp/static/images/movies_images") + "\\"
f = open("movies.json", 'r')

names = {}
for line in f:
    data = json.loads(line)
    imgname = re.sub(r"\W", "", data["title"])
    newName = secrets.token_hex(8) + ".jpg"
    names.update({imgname: newName})
    urllib.request.urlretrieve(data["image"], path + newName)

f.close()

with open("names.json", 'w') as n:    
    json.dump(names, n)
    