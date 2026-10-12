import * as BookList from '../model/booklist_model.mjs' // version 3 with ORM sequelize, postgress

async function showBookList(req, res, next) {
    try {
        const myBooks = await BookList.loadBooks(req.session.username)
        res.render("booklist", { books: myBooks })
    } catch (error) {
        next(error)
    }
}

const addBook = async (req, res, next) => {

    try {
        await BookList.addBook({
            "title": req.body["newBookTitle"],
            "author": req.body["newBookAuthor"]
        }, req.session.username)
        next() //επόμενο middleware είναι το showBookList
    }
    catch (error) {
        next(error) //αν έγινε σφάλμα, με το next(error) θα κληθεί το middleware με τις παραμέτρους (error, req, res, next)
    }
}

const deleteBook = async (req, res, next) => {
    const title = req.params.title;
    const user = req.session.username;
    try {
        await BookList.deleteBook({ title: title }, user)
        next() //επόμενο middleware είναι το BookController.showBookList
    }
    catch (error) {
        next(error)//αν έγινε σφάλμα, με το next(error) θα κληθεί το middleware με τις παραμέτρους (error, req, res, next)
    }
}

async function showComments (req, res, next) {
    try {
        const book = await BookList.getBook(req.params.bookID)
        let comments = await BookList.getAllComments(req.params.bookID);
        comments = Object.values(comments).filter(entry => entry.UserName != req.session.username);
        // do not display user's own comments
        const userComment = await BookList.getComment(req.params.bookID, req.session.username);
        res.render("comment", { book: book, comments: comments, userComment: userComment })
    } catch (error) {
        next(error)
    }
}

const addComment = async(req, res, next) => {
    const text = req.body.comment;
    const user = req.session.username;
    const bookID = req.params.bookID;
    try {
        await BookList.addComment(text, user, bookID)
        next() //επόμενο middleware είναι το BookController.showBookList
    }
    catch (error) {
        console.log(error);
        next(error)//αν έγινε σφάλμα, με το next(error) θα κληθεί το middleware με τις παραμέτρους (error, req, res, next)
    }
}

export { showBookList, addBook, deleteBook, showComments, addComment }
