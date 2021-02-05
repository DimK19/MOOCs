// Τα ερωτήματα 2 έως 7 θα απαντηθούν στο αρχείο αυτό

const newGuess = document.querySelector("#new-guess");
const message = document.querySelector("#message");
const lowHigh = document.querySelector("#low-high");
const checkButton = document.querySelector("#check");
const restartButton = document.querySelector("#restart");
const root = document.querySelector(":root");

// ορίζουμε σταθερές για αποθήκευση των χρωμάτων από το css αρχείο
const winColor = getComputedStyle(root).getPropertyValue("--msg-win-color");
const wrongColor = getComputedStyle(root).getPropertyValue("--msg-wrong-color");

// 2. να ορίσετε τους σχετικούς χειριστές συμβάντων
newGuess.addEventListener("keyup", checkKey);
checkButton.addEventListener("click", checkGuess);
restartButton.addEventListener("click", restart);

let previousGuesses = [];
let theGuess;
window.onload = newRandom();
newGuess.focus();


function newRandom()
{
/* 3. συνάρτηση που βρίσκει ένα τυχαίο αριθμό μεταξύ 1 και 100
και τον εκχωρεί στη μεταβλητή theGuess */
    theGuess = Math.floor((Math.random() * 100) + 1);
}

function checkKey(e)
{
/* 4. συνάρτηση που όταν ο χρήστης πατήσει <<enter>>
 * να καλεί τη συνάρτηση που αποτελεί τον κεντρικό ελεγκτή του παιχνιδιού. */
    if(e.keyCode == 13) checkGuess(); // σε keycode 13 αντιστοιχεί το enter
}

function checkGuess()
{
/* 5. Να ορίσετε συνάρτηση checkGuess η οποία καλείται είτε όταν ο χρήστης πατήσει <<enter>>
στο πεδίο "new-guess" είτε όταν πατήσει το πλήκτρο "check", η οποία είναι ο κεντρικός ελεγκτής,
καλεί τη συνάρτηση processGuess (η οποία αποφαίνεται για την ορθότητα του αριθμού) και κάνει
τις κατάλληλες ενέργειες για να μην μπορεί να εισάγει ο χρήστης νέο αριθμό ή να ανασταλεί η
λειτουργία του <<enter>>, εμφάνιση του πλήκτρου 'restart' και την εξαφάνιση του πλήκτρου 'check'
σε περίπτωση ολοκλήρωσης του παιχνιδιού. */
    
    // παίρνουμε την είσοδο που γράφει ο χρήστης στο textbox, και μετά το αδειάζουμε
    let userInput = newGuess.value;
    newGuess.value = "";
    
    // στην παρακάτω μεταβλητή αποθηκεύεται το αποτέλεσμα που επιστρέφει η processGuess, όταν σε αυτήν δίνεται είσοδος
    // η μαντεψιά του χρήστη σε ακέραια μορφή
    let answer = processGuess(userInput);

    if(answer == "win" || answer == "lost")
    {
        restartButton.style.display = "block";
        checkButton.style.display = "none";
        newGuess.disabled = true; // όταν τελειώσει το παιχνίδι, όσο ο χρήστης δεν έχει πατήσει "Παίξε ξανά", δεν πρέπει να μπορεί να εισάγει αριθμό.
    }
}

function processGuess(newValue)
{
 /* 6.  Να ορίσετε συνάρτηση processGuess(newValue) η οποία καλείται από τη συνάρτηση checkGuess,
 περιέχει τη λογική του παιχνιδιού, ελέγχει αν η τιμή του χρήστη είναι σωστή, ή αν το παιχνίδι έχει
 τελειώσει χωρίς ο χρήστης να έχει βρει τον αριθμό, και επιστρέφει αντίστοιχα την τιμή "win", ή "lost",
 δημιουργεί και εμφανίζει τα κατάλληλα μηνύματα, αλλάζοντας το χρώμα του στοιχείου μηνυμάτων.
 Όλα τα μηνύματα του προγράμματος εμφανίζονται από την processGuess().
 Σε περίπτωση που το παιχνίδι δεν έχει ακόμα τελειώσει, η συνάρτηση μπορεί είτε να μην επιστρέφει κάποια ιδιαίτερη τιμή,
 είτε να επιστρέφει κάποια τιμή της επιλογής σας */

    // μετατροπή σε ακέραιο
    let inputValue = parseInt(newValue, 10);
    
    /*
    καλό είναι η parseInt να δηλώνεται έτσι (με παράμετρο το 10) για να δέχεται αριθμούς στο δεκαδικό σύστημα.
    Αλλιώς την είσοδο π.χ. 0xff την δέχεται σαν δεκαεξαδική τιμή

    parseInt("0xff")
    255
    parseInt("0xff",10)
    0

    */
    
    // έλεγχος αν η είσοδος είναι έγκυρη (αριθμός)
    // κάνουμε την παραδοχή ότι αν ο χρήστης εισάγει μη έγκυρη μαντεψιά (δηλαδή όχι αριθμό), δεν θα μετρήσει στις αποτυχημένες προσπάθειες.
    if(newValue == "" || !isFinite(inputValue))
    {
        message.innerHTML = "Δώσε αριθμό!";
        changeBgColor(wrongColor);
        return "error"; // εδώ η τιμή επιστροφής δεν έχει σημασία, αλλά επιστρέφουμε μια συμβολοσειρά που περιγράφει το
        // αποτέλεσμα, για λόγους συνέπειας. Το ίδιο και όταν η μαντεψιά είναι μεγαλύτερη ("high") και μικρότερη ("low").
    }
 
    // εισάγουμε την μαντεψιά στον πίνακα, και εμφανίζουμε την λίστα με όλες τις προσπάθειες
    previousGuesses.push(inputValue);
    lowHigh.innerHTML = printArray(previousGuesses); // η υλοποίηση της συνάρτησης printArray στο τέλος του αρχείου
    
    if(inputValue == theGuess)
    {
        message.innerHTML = "Μπράβο, το βρήκες!";
        changeBgColor(winColor);
        return "win";
    }
    else if(previousGuesses.length == 10)
    {
        message.innerHTML = "Τέλος παιχνιδιού, έχασες!";
        changeBgColor(wrongColor);
        return "lost";
    }
    else if(inputValue > theGuess)
    {
        message.innerHTML = "Λάθος, το ξεπέρασες";
        changeBgColor(wrongColor);
        return "high";
    }
    else // inputValue < theGuess
    {
        message.innerHTML = "Λάθος, είσαι χαμηλά";
        changeBgColor(wrongColor);
        return "low";
    }
}
function restart()
{
/* 7. Να ορίσετε συνάρτηση restart η οποία καλείται όταν ο χρήστης πατήσει το πλήκτρο
'restart' και επανεκινεί τη διαδικασία */
    previousGuesses = [];
    newRandom();
    newGuess.disabled = false;
    restartButton.style.display = "none";
    checkButton.style.display = "inline";
    message.innerHTML = "";
    lowHigh.innerHTML = "";
}

// Συνάρτηση που επιστρέφει μια συμβολοσειρά με τις προηγούμενες προσπάθειες στην επιθυμητή μορφή
function printArray(arr)
{
    let result = "Προηγούμενες προσπάθειες: ";
    for(var i = 0; i < arr.length; i++)
    {
        result += arr[i] + ' ';
    }
    
    return result;
}

// Συνάρτηση που αλλάζει το χρώμα υποβάθρου του μηνύματος
function changeBgColor(c)
{
    message.style.background = c;
}
