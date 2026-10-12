## https://webapps.stackexchange.com/a/51355
## https://python.plainenglish.io/how-to-scrape-imdb-data-9d7535b98576
## https://stackoverflow.com/questions/151272/given-an-imdb-movie-id-how-do-i-programmatically-get-its-poster-image/

import requests
from bs4 import BeautifulSoup
import re
import json
import random
from imdb import IMDb

imdb = IMDb()

url = "https://www.imdb.com/search/title/?companies=co0051941"

def getMovieTitle(header):
    try:
        return header[0].find("a").getText()
    except:
        return "NA"

def getReleaseYear(header):
    try:
        return header[0].find("span",  {"class": "lister-item-year text-muted unbold"}).getText()
    except:
        return "NA"

def getGenre(muted_text):
    try:
        return muted_text.find("span",  {"class":  "genre"}).getText()
    except:
        return "NA"

def getSynopsis(movie, titleID):
    ## summaryURL = f"https://www.imdb.com/title/{titleID}/plotsummary"
    try:
        m = imdb.get_movie(titleID[2:])
        return m.data["synopsis"][0]
    except KeyError:
        ## synopsis does not exist, get summary instead
        pass

    try:
        return movie.find_all("p", {"class":  "text-muted"})[1].getText()
    except:
        return "NA"

def getImage(image):
    try:
        return image.get("loadlate")
    except:
        return "NA"

def getImageId(image):
    try:
        return image.get("data-tconst")
    except:
        return "NA"

def foo(imdb_url):
    headers = {"Accept-Language": "en"}
    response = requests.get(imdb_url, headers = headers)
    soup = BeautifulSoup(response.text, "html.parser")

    # Movie Name
    movies_list  = soup.find_all("div", {"class": "lister-item mode-advanced"})

    entries = []
    for movie in movies_list:
        entry = {}
        header = movie.find_all("h3", {"class":  "lister-item-header"})
        muted_text = movie.find_all("p", {"class":  "text-muted"})[0]
        imageDiv =  movie.find("div", {"class": "lister-item-image float-left"})
        image = imageDiv.find("img", "loadlate")

        #  Movie Title
        movie_title =  getMovieTitle(header)
        entry["title"] = movie_title

        #  Movie release year
        year = getReleaseYear(header)
        year = re.sub(r"\D", "", year)
        entry["release_year"] = int(year) if len(year) == 4 else 2025

        #  Image attributes
        imgURL = getImage(image)
        titleID = image.get("data-tconst") ## this is also the title's id tt0000000

        # Movie Synopsis
        synopsis = getSynopsis(movie, titleID)
        entry["plot"] = synopsis.strip().replace('\"', '')

        ## change in-name specifications to get higher resolution version
        entry["image"] = re.sub(r"\._V1(.*)", "._V1_QL75_UX380_CR0,0,380,562_.jpg", imgURL)

        entry["rating"] = random.randint(1, 100)
        entries.append(entry)


with open("movies.json", 'w', encoding = "utf-8") as f:
    for e in foo(url):
        json.dump(e, f)
        f.write("\n")
