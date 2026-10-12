/* TODO
 * better next round panel
 * progress bar dimensions
 */

let data = '';
let sessionData = new Array();
let alreadyFetched = new Array();
const requestURL = "https://restcountries.eu/rest/v2/alpha/";
// Ένας βοηθητικός πίνακας που θα περιέχει τις χώρες από το countries.js σε λίγο πιο βολική μορφή
const allCountries = new Array();
// πχ. allCountries['GRE'] = { code2: "GR", name: "Greece"}, ή κάτι παρόμοιο 
for(var c = 0; c < countryObjects.length; c++)
{
	// allCountries.push({code: countryObjects[c]["code"], code3: countryObjects[c]["code3"], name: countryObjects[c]["name"]})
	allCountries[countryObjects[c]["code3"]] = {code2: countryObjects[c]["code"], name: countryObjects[c]["name"]};
}

// Εδώ μπορείτε να βάλετε τον κώδικα για όλο το παιχνίδι
// Μπορείτε γράψετε κλάσεις που να έχουν κάποια συμπεριφορά, 
// π.χ. Game, PlayingCountry, neighbor, ...
// ή να και ακολουθήσετε άλλη τακτική, που να σας είναι πιο προσιτή

async function postData(url = '') {
	const response = await fetch(url);
	return response.json(); // parses JSON response into native JavaScript objects
}

// Επιλογή τυχαίας χώρας
function getRandomElement(array)
{
	var index = Math.floor(Math.random() * array.length); // επιστρέφει τυχαίο αριθμό μεταξύ 0 και το πλήθος των στοιχείων του πίνακα - 1
	return array[index];
}

class Game {
	constructor()
	{
		this.score = 0;
		this.roundsPlayed = 0;
		this.alreadySelected = [];
	}
	
	getScore()
	{
		return this.score;
	}
	
	updateScore()
	{
		document.querySelector('#score').textContent = "Σκορ: " + this.score;
	}
	
	roundOver(outcome)
	{
		document.getElementById('neighbors-panel').style.pointerEvents = 'none';
		document.getElementById('neighbors-panel').style.opacity = '0.5';
		document.getElementById('neighbors-panel').style.backgroundColor = 'darkgray';
		let message = "";
		if(outcome === 0) message = "LOSE";
		else if(outcome === 1) message = "WIN";
		document.getElementById('result-text').innerHTML = message;
	}
	
	dataDisplay(response){
		data = response['borders'];//ενημερώνεται με το τέλος της ασύγχρονης fetch
		
		alreadyFetched.push(response['alpha3Code']);
		sessionData[response['alpha3Code']] = response;
		if(response['borders'].length < 1)
		{
			console.log('no neighbors');
			this.nextRound();
		}
		else
		{
			this.roundsPlayed += 1;
			var correctAttempts = 0, wrongAttempts = 0;
			document.querySelector('#round').textContent = "Γύρος: " + this.roundsPlayed;
			
			// PLAY GAME
			var neighbors = response["borders"];
			
			document.querySelector("#my-country-flag").innerHTML = country2emoji2(response["alpha2Code"]);
				
			document.querySelector("#my-country-name").innerHTML = response["name"];
			
			// populate list with fake neighbors
			var allFlags = [...neighbors];
			while(allFlags.length < 3 * neighbors.length)
			{
				var c = getRandomElement(countryObjects)["code3"];
				if(!allFlags.includes(c) && c !== response["alpha3Code"]) allFlags.push(c);
			}
			
			allFlags = shuffleArray(allFlags);
			
			for(var i = 0; i < allFlags.length; i++)
			{
				var option = document.createElement('div');
				option.className = "neighbor";
				var f = document.createElement('p');
				var name = document.createElement('p');
				f.innerHTML = country2emoji2(allCountries[allFlags[i]]["code2"]);
				name.innerHTML = allCountries[allFlags[i]]["name"];
				f.className = "neighbor-flag";
				name.className = "subtitle";
				option.appendChild(f);
				option.appendChild(name);
				if(neighbors.includes(allFlags[i])) option.classList.add("neighbor-is-valid");
				else option.classList.add("neighbor-is-invalid");
				option.addEventListener("click", (e) => {
					var element = e.target.parentElement;
					e.target.parentElement.classList.add("was-clicked");
					if(element.classList.contains("neighbor-is-valid"))
					{
						correctAttempts += 1;
						this.score += 5;
						// progress bar
						let v = 100 * correctAttempts / neighbors.length;
						document.getElementById('progress-bar').setAttribute('value', v.toString());
					}
					else
					{
						wrongAttempts += 1;
						this.score -= 3;
					}
					this.updateScore();
					
					if(correctAttempts === neighbors.length)
					{
						console.log('WIN');
						this.roundOver(1);
					}
					else if(wrongAttempts === 2 * neighbors.length || correctAttempts + wrongAttempts === 10)
					{
						console.log('LOSE');
						this.roundOver(0);
					}
					
				})
				document.querySelector("#neighbors-panel").appendChild(option);
			}
		}
	}
	
	
	async nextRound()
	{
		// UNDO ROUND RESULT
		document.getElementById('neighbors-panel').style.pointerEvents = 'auto';
		document.getElementById('neighbors-panel').style.opacity = '1';
		document.getElementById('neighbors-panel').style.backgroundColor = 'white';
		document.getElementById('result-text').innerHTML = '';
		document.getElementById('progress-bar').setAttribute('value', '0');
		// ------------
		document.querySelector("#neighbors-panel").textContent = '';
		var neighbors = [], country;
		
		do
		{
			country = getRandomElement(countryObjects)["code3"];
		}
		while(this.alreadySelected.includes(country));
		this.alreadySelected.push(country);
		
		if(alreadyFetched.includes(country)) dataDisplay(sessionData[country])
		else
		{
			await fetch(requestURL + country)
			.then(response => {
				if (response.status==200) //αν η ιδιότητα status του response είναι 200 τότε βρήκε δεδομένα
					return response.json();//τότε επιστρέφω ένα promise με τα δεδομένα που πήρα από το url με την fetch(στο promise2)
				else
					throw new Error("Δεν το βρήκα!");//αλλιώς στέλνω ένα error
			})
			.then(data => {
				this.dataDisplay(data);
			})
			.catch(error => console.log(error));
			/*
			var promise1=fetch(requestURL + country);//διαβάζει τα περιεχόμενα του url και επιστρέφει ένα promise που το βάζω στην μεταβλητή promise1
			var promise2=promise1.then(response=>{//από το promise1 με το then διαβάζω την απάντηση του fetch στo object response
				console.log("1->"+response.status);//to response έχει την μέθοδο status που μου λέει αν βρέθηκαν τα δεδομένα που ήθελα στο url
				if (response.status==200) //αν η ιδιότητα status του response είναι 200 τότε βρήκε δεδομένα
					return response.json();//τότε επιστρέφω ένα promise με τα δεδομένα που πήρα από το url με την fetch(στο promise2)
				else
					throw new Error("Δεν το βρήκα!");//αλλιώς στέλνω ένα error
				});
			var promise3=promise2.then(response => {//από το promise2 με το then διαβάζω την επιστροφή του promise1.then--> (response.json)
				console.log({"2->":response});
				console.log("3->"+response.name);
				// data1 = response.name;//δίνω δεδομένα στην global μεταβλητή
				this.dataDisplay(response);//εκτελώ μία συνάρτηση με παράμετρο τα δεδομένα που διάβασα μόλις ολοκληρωθεί το fetch
			});
			promise1.catch(error=>console.log("4->"+error));//αυτό τρέχει αν αποτύχει με σφάλμα το promise1 το fetch δηλαδή
			promise2.catch(error=>console.log("5->"+error));//εδώ πιάνει το error από το promise2
			promise3.catch(error=>console.log("6->"+error));//εδώ πιάνει το error από το promise3
			*/
		}
		//console.log("data(2)=" + data);//δεν έχει προλάβει να ενημερωθεί γιατί το fetch εκτελείται ασύγχρονα μετά το τέλος του κώδικα
	}
}

document.addEventListener("DOMContentLoaded", () => {
	//start a new Game
	//...
	var game = new Game();
	game.nextRound();
	//event listener to new game button
	document.querySelector("#btn-new-game").addEventListener("click", () => {
		// ...
		if(window.confirm("confirmation"))
		{
			game = new Game();
			game.updateScore();
			game.nextRound();
		}
	})

	//event listener to next round button
	document.querySelector("#btn-next-round").addEventListener("click", () => {
		// ...
		game.nextRound();
	})

});
