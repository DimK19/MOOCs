import express from 'express'
// use different models to demonstrate connection to different databases
import * as Validator from './validator/validation.mjs'
import * as UserController from './controller/user_controller.mjs'
import * as BookController from './controller/book_controller.mjs'

const router = express.Router()

//αν υπάρχει ενεργή συνεδρία, ανακατεύθυνε στο /books
router.get("/", (req, res) => {
    if (req.session.username)
        res.redirect("/books")
    else
        res.render("home")
})

//έλεγξε αν έχει συνδεθεί ο χρήστης, μετά δείξε τα βιβλία
router.get("/books", UserController.checkIfAuthenticated, BookController.showBookList)

//δείξε τη φόρμα εισαγωγής νέου βιβλίου
router.get("/addbookform", UserController.checkIfAuthenticated, (req, res) => {
    res.render("addbookform")
})

//αυτή η διαδρομή υποδέχεται τη φόρμα εισόδου
router.post("/books",
    // UserController.checkIfAuthenticated,
    Validator.validateLogin,
    UserController.doLogin,
    BookController.showBookList)

//υποδέχεται την φόρμα υποβολής νέου βιβλίου
router.post("/doaddbook",
    UserController.checkIfAuthenticated, //έλεγξε αν έχει συνδεθεί ο χρήστης,
    Validator.validateNewBook,
    BookController.addBook,
    BookController.showBookList)

/* Η ΔΗΜΟΣΙΕΥΣΗ ΈΧΕΙ ΓΙΝΕΙ ΣΤΟ HEROKU ΣΤΟΝ ΣΥΝΔΕΣΜΟ mathesis-33.herokuapp.com
 * ΔΗΜΙΟΥΡΓΗΜΕΝΟΙ ΧΡΗΣΤΕΣ | ΚΩΔΙΚΟΙ
 * "Kratos"               | "1234"
 * "Agent_47"             | "1234"
 * "Arthur_Morgan"        | "1234"
 * "MasterChief"          | "1234"
 */

// εμφάνιση σχολίων για συγκεκριμένο τίτλο
router.get("/comment/:bookID",
    UserController.checkIfAuthenticated,
    BookController.showComments);

// εισαγωγή νέου σχολίου
router.post("/comment/:bookID",
    UserController.checkIfAuthenticated,
    Validator.validateNewComment,
    BookController.addComment,
    BookController.showBookList);

// διαγραφή ενός βιβλίου με τίτλο title από τη λίστα του χρήστη
router.get("/delete/:title",
    UserController.checkIfAuthenticated, //έλεγξε αν έχει συνδεθεί ο χρήστης,
    BookController.deleteBook,
    BookController.showBookList);

router.get("/logout", UserController.doLogout, (req, res) => {
    // req.session.destroy() //καταστρέφουμε τη συνεδρία στο session store
    res.redirect("/")
})

router.get("/register", (req, res) => {
    res.render("registrationform")
})

router.post("/doregister",
    Validator.validateNewUser,
    UserController.doRegister)

export { router }
