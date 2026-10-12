/* ΣΗΜΕΙΩΣΗ!
https://restcountries.com/v3.1/alpha/BRN?fields=cca2,cca3,name,borders

το URI για την χώρα Brunei επιστρέφει λανθασμένη απόκριση - μη έγκυρο JSON και δημιουργεί πρόβλημα.

Επίσης το API έχει μπερδεμένους κωδικούς για το Κοσσυφοπέδιο: https://restcountries.com/v3.1/alpha/KOS
στο URI χρησιμοποιεί αυτόν αλλά στο response το πεδίο cca3 έχει τιμή 'UNK'.
Και στις χώρες που είναι παρόν ως γείτονας επίσης συμβολίζεται με 'UNK' πχ εδώ:
https://restcountries.com/v3.1/alpha/SRB?fields=cca2,cca3,name,borders.
Στο δεδομένο αρχείο countries.js έχει code3 το 'KOS'.

ΕΠΟΜΕΝΩΣ

Το δεύτερο πρόβλημα μπορεί να αντιμετωπισθεί με κατάλληλη αλλαγή στο αρχείο countries.js,
αλλά και τα δύο βασικά πηγάζουν από το API, οπότε ας μην θεωρηθούν λάθη της εργασίας. 
*/

let data = '';
const requestURL = 'https://restcountries.com/v3.1/alpha/';
// Ένα βοηθητικό object (σαν λεξικό) που θα περιέχει τις χώρες από το countries.js σε πιο βολική μορφή
const allCountries = new Object();
// πχ. allCountries['GRE'] = { code2: 'GR', name: 'Greece'}, ή κάτι παρόμοιο
// Επειδή το αρχείο αυτό φορτώνεται από τον browser ύστερα από το countries.js, βρίσκει από αυτό τον πίνακα countryObjects
for(var c = 0; c < countryObjects.length; c++) {
	// allCountries.push({code: countryObjects[c]['code'], code3: countryObjects[c]['code3'], name: countryObjects[c]['name']})
	allCountries[countryObjects[c]['code3']] = {code2: countryObjects[c]['code'], name: countryObjects[c]['name']};
}

// Εδώ μπορείτε να βάλετε τον κώδικα για όλο το παιχνίδι
// Μπορείτε γράψετε κλάσεις που να έχουν κάποια συμπεριφορά,
// π.χ. Game, PlayingCountry, neighbor, ...
// ή να και ακολουθήσετε άλλη τακτική, που να σας είναι πιο προσιτή

// Επιλογή τυχαίας χώρας
function getRandomElement(array) {
	var index = Math.floor(Math.random() * array.length); // επιστρέφει τυχαίο αριθμό μεταξύ 0 και το πλήθος των στοιχείων του πίνακα - 1
	return array[index];
}

class Game {
	constructor() {
		this.score = 0;
		this.roundsPlayed = 0;
		this.alreadySelected = [];
	}

	getScore() {
		return this.score;
	}

	updateScore() {
		document.querySelector('#score').textContent = this.score;
	}

	// οι πιθανοί κωδικοί για το outcome που επιστρέφονται από την playRound είναι 1 για νίκη και 0 για ήττα
	roundOver(outcome) {
		document.getElementById('neighbors-panel').style.pointerEvents = 'none';
		let message = '';
		if(outcome === 0) {
			message = 'Κρίμα, χάσατε';
			document.getElementById('next-round-panel').style.color = 'maroon';
		}
		else if(outcome === 1) {
			message = 'Τους βρήκατε όλους!';
			document.getElementById('next-round-panel').style.color = 'black';
		}
		document.getElementById('next-round-panel').innerHTML = message;
		document.getElementById('next-round-panel').style.display = 'flex';
		document.getElementById('btn-next-round').disabled = false;
	}

	playRound(response) {
		// αποθήκευση της απόκρισης ώστε να μην χρειαστεί να αποσταλεί ξανά request αν ξανατύχει η ίδια χώρα σε new game
		sessionStorage.setItem(response['cca3'], JSON.stringify(response));

		this.roundsPlayed += 1;
		var correctAttempts = 0, wrongAttempts = 0;
		document.querySelector('#round').textContent = this.roundsPlayed;

		// PLAY GAME
		var neighbors = response['borders'];
		// εκτύπωση των πραγματικών γειτόνων για διευκόλυνση στην διόρθωση / αποσφαλμάτωση
		// console.log(neighbors);

		document.querySelector('#my-country-flag').innerHTML = country2emoji2(response['cca2']);

		document.querySelector('#my-country-name').innerHTML = response['name']['common'];

		// populate list with fake neighbors
		var allFlags = [...neighbors];
		let flagCounter = 2 * neighbors.length;
		while(flagCounter > 0) {
			flagCounter--;
			var c = getRandomElement(countryObjects)['code3'];
			if(!allFlags.includes(c) && c !== response['cca3']) allFlags.push(c);
		}

		allFlags = shuffleArray(allFlags);

		// κατασκευή ενός στοιχείου neighbor για κάθε μέλος της λίστας
		for(var i = 0; i < allFlags.length; i++) {
			var countryOption = document.createElement('div');
			countryOption.className = 'neighbor';
			var f = document.createElement('span');
			var name = document.createElement('span');
			f.innerHTML = country2emoji2(allCountries[allFlags[i]]['code2']);
			name.innerHTML = allCountries[allFlags[i]]['name'];
			f.className = 'neighbor-flag';
			name.className = 'subtitle';
			countryOption.appendChild(f);
			countryOption.appendChild(name);
			if(neighbors.includes(allFlags[i])) countryOption.classList.add('neighbor-is-valid');
			else countryOption.classList.add('neighbor-is-invalid');
			// κάθε στοιχείο συνδέεται με ξεχωριστή συνάρτηση event listener, η οποία εκτελείται μόνο στο πρώτο κλικ
			// για κάθε πιθανό γείτονα, χάρη στην παράμετρο {once: true}
			countryOption.addEventListener('click', (e) => {
				var element = e.target.parentElement;
				e.target.parentElement.classList.add('was-clicked');
				if(element.classList.contains('neighbor-is-valid')) {
					correctAttempts += 1;
					this.score += 5;
					// progress bar
					let v = 100 * correctAttempts / neighbors.length;
					document.getElementById('progress-bar').setAttribute('value', v.toString());
				}
				else {
					wrongAttempts += 1;
					this.score -= 3;
				}
				this.updateScore();

				if(correctAttempts === neighbors.length) {
					// console.log('WIN');
					this.roundOver(1);
				}
				else if(wrongAttempts === neighbors.length) {
					// console.log('LOSE');
					this.roundOver(0);
				}

			}, {once: true});
			document.querySelector('#neighbors-panel').appendChild(countryOption);
		}
	}


	async nextRound() {
		// UNDO PREVIOUS ROUND RESULT
		document.getElementById('my-country-flag').textContent = '';
		document.getElementById('my-country-name').textContent = '';
		document.getElementById('neighbors-panel').style.pointerEvents = 'auto';
		document.getElementById('neighbors-panel').style.opacity = '1';
		document.getElementById('neighbors-panel').style.backgroundColor = 'white';
		document.getElementById('next-round-panel').style.display = 'none';
		document.getElementById('progress-bar').setAttribute('value', '0');
		// https://stackoverflow.com/a/65413839
		document.querySelector('#neighbors-panel').replaceChildren();
		document.getElementById('btn-next-round').disabled = true;
		
		// ΕΠΙΛΟΓΗ ΧΩΡΑΣ ΠΟΥ ΔΕΝ ΕΧΕΙ ΗΔΗ ΕΠΙΛΕΓΕΙ ΣΤΟ ΤΡΕΧΟΝ ΠΑΙΓΝΙΔΙ
		var neighbors = [], country;
		do {
			country = getRandomElement(countryObjects)['code3'];
		}
		while(this.alreadySelected.includes(country));
		this.alreadySelected.push(country);
		
		// ΑΝ ΈΧΕΙ ΑΠΟΘΗΚΕΥΘΕΙ ΑΠΟ ΠΡΟΗΓΟΥΜΕΝΟ ΠΑΙΓΝΙΔΙ ΔΕΝ ΧΡΕΙΑΖΕΤΑΙ ΝΑ ΑΠΟΣΤΑΛΕΙ ΞΑΝΑ ΑΙΤΗΜΑ ΣΤΟ API
		if(sessionStorage.getItem(country) !== null) {
			// if(country in sessionStorage)
			this.playRound(JSON.parse(sessionStorage.getItem(country)));
		}
		else {
			// console.log(requestURL + country + '?fields=cca2,cca3,name,borders');
			await fetch(requestURL + country + '?fields=cca2,cca3,name,borders')
			.then(response => {
				if(response.status == 200) //αν η ιδιότητα status του response είναι 200 τότε βρήκε δεδομένα
					return response.json(); //τότε επιστρέφω ένα promise με τα δεδομένα που πήρα από το url με την fetch
				else
					throw new Error(response.status); //αλλιώς στέλνω ένα error
			})
			.then(res => {
				// Αν η χώρα δεν έχει γειτονικές καλείται ξανά η συνάρτηση
				// οπότε επαναλαμβάνεται η ίδια διαδικασία με τον 'γύρο' αυτόν ως μη γενόμενο
				// έως ότου βρεθεί κατάλληλη χώρα
				if(res['borders'].length < 1) return this.nextRound();
				// console.log(`${JSON.stringify(res)}`);
				this.playRound(res);
			})
			.catch(error => console.log(error));
		}
	}
}

document.addEventListener('DOMContentLoaded', () => {
	//start a new Game
	var game = new Game();
	game.nextRound();
	//event listener to new game button
	document.querySelector('#btn-new-game').addEventListener('click', () => {
		// console.log(sessionStorage);
		if(window.confirm('Σίγουρα; Θα χάσετε όλο σας το σκορ!')) {
			game = new Game();
			game.updateScore();
			game.nextRound();
		}
	})

	//event listener to next round button
	document.querySelector('#btn-next-round').addEventListener('click', () => {
		game.nextRound();
	})

});
