import { Book, User, BookUser } from './bookList_seq_pg.mjs'
import bcrypt from 'bcrypt'
import { Sequelize, Op } from 'sequelize'

async function addUser(username, password) {
    try {
        if (!username || !password)
            throw new Error("Λείπει το όνομα ή το συνθηματικό του χρήστη")

        let user = await User.findOne({ where: { name: username } })

        if (user)
            throw new Error("Υπάρχει ήδη χρήστης με όνομα " + username)

        const hash = await bcrypt.hash(password, 10)
        user = await User.create({ name: username, password: hash })
        return user
    } catch (error) {
        throw error
    }
}

async function login(username, password) {
    try {
        if (!username || !password)
            throw new Error("Λείπει το όνομα ή το συνθηματικό του χρήστη")

        let user = await User.findOne({ where: { name: username } })

        if (!user)
            throw new Error("Δεν υπάρχει χρήστης με όνομα " + username)

        const match = await bcrypt.compare(password, user.password)
        if (match)
            return user
        else
            throw new Error("Λάθος στοιχεία πρόσβασης")
    } catch (error) {
        throw error
    }
}

async function loadBooks(username) {
    try {
        if (!username)
            throw new Error("Πρέπει να δοθεί όνομα χρήστη")

        const user = await User.findOne({ where: { name: username } });
        if (!user)
            throw new Error("άγνωστος χρήστης")

        const myBooks = await user.getBooks({ raw: true }); //με raw επιστρέφεται το "καθαρό" αντικείμενο (ο πίνακας) χωρίς πληροφορίες που αφορούν τη sequelize
        return myBooks
    } catch (error) {
        throw error
    }
}

async function addBook(newBook, username) {
    try {
        if (!username)
            throw new Error("Πρέπει να δοθεί όνομα χρήστη")

        const user = await User.findOne({ where: { name: username } });
        if (!user)
            throw new Error("άγνωστος χρήστης")

        const [book, created] = await Book.findOrCreate({
            where: {
                title: newBook.title,
                author: newBook.author
            }
        })
        await user.addBook(book)
    } catch (error) {
        throw error
    }
}

async function getBook(bookID) {
    return await Book.findOne({ where: { title: bookID }, raw: true })
}

// το βιβλίο διαγράφεται από τον πίνακα "BookUser"
async function deleteBook(book, user) {
    try {
        // await this.findOrAddUser()
        const bookToRemove = await Book.findOne(
            { where: { title: book.title } }
        )
        await bookToRemove.removeUser(user)
        // this.user.removeBook(bookToRemove) // εναλλακτικά, από την πλευρά του User

        // BookUser.destroy({ where: { BookTitle: "test5", UserName: "masterchief" } })

        // Αν δεν υπάρχουν άλλοι χρήστες, διαγράφουμε το βιβλίο
        const numberOfUsers = await bookToRemove.countUsers()
        if (numberOfUsers == 0) {
            Book.destroy(
                { where: { title: book.title } }
            )
        }
    } catch (error) {
        throw error
    }
}

async function findOrAddUser() {
    //στο this.user θα φυλάσσεται το αντικείμενο που αντιπροσωπεύει τον χρήστη στη sequelize
    //αν δεν υπάρχει, τότε το ανακτούμε από τη sequalize
    //αλλιώς, υπάρχει το this.user και δεν χρειάζεται να κάνουμε κάτι άλλο
    if (this.user == undefined)
        try {
            const [user, created] = await User.findOrCreate({ where: { name: this.username } })
            this.user = user
        } catch (error) {
            throw error
        }
}

// create comment
async function addComment(newComment, userID, bookID) {
    try {
        if (!userID)
            throw new Error("Πρέπει να δοθεί όνομα χρήστη")
        // για την περίπτωση που το αίτημα δεν γίνεται μέσω του
        // frontend αλλά απ' ευθείας στο backend
        else if (!bookID)
            throw new Error("Πρέπει να δοθεί τίτλος βιβλίου")

        const user = await User.findOne({ where: { name: userID } });
        const book = await Book.findOne({ where: { title: bookID } });

        if (!user)
            throw new Error("άγνωστος χρήστης")
        else if(!book)
            throw new Error("άγνωστος τίτλος")

        await BookUser.upsert({
            UserName: userID,
            BookTitle: bookID,
            comment: newComment
        })
    } catch (error) {
        throw error
    }
}

// return comment on book title by user - may be null
async function getComment(bookTitle, userName) {
    try {
        if (!bookTitle)
            throw new Error("Πρέπει να δοθεί τίτλος")
        else if (!userName)
            throw new Error("Πρέπει να δοθεί όνομα χρήστη")

        const bookuser = await BookUser.findOne({ where: { BookTitle: bookTitle, UserName: userName} , raw: true });

        return bookuser.comment;
        //return await bookuser.getBookUsers({ raw: true }); //με raw επιστρέφεται το "καθαρό" αντικείμενο (ο πίνακας) χωρίς πληροφορίες που αφορούν τη sequelize
    } catch (error) {
        throw error
    }
}

// return all comments under specific book title
async function getAllComments(bookTitle) {
    try {
        if (!bookTitle)
            throw new Error("Πρέπει να δοθεί τίτλος")

        const bookuser = await BookUser.findAll({ where: { BookTitle: bookTitle, comment: { [Op.not]: null } }, raw: true });
        if (!bookuser)
            throw new Error("σφάλμα")

        return bookuser
        //return await bookuser.getBookUsers({ raw: true }); //με raw επιστρέφεται το "καθαρό" αντικείμενο (ο πίνακας) χωρίς πληροφορίες που αφορούν τη sequelize
    } catch (error) {
        throw error
    }
}

export { addUser, login, loadBooks, addBook, deleteBook, addComment, getAllComments, getComment, findOrAddUser, getBook }
