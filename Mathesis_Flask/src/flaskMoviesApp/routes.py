from flask import render_template, redirect, url_for, request, flash, abort
from flaskMoviesApp.forms import SignupForm, LoginForm, NewMovieForm, AccountUpdateForm
from flaskMoviesApp import app, db, bcrypt
from flaskMoviesApp.models import User, Movie
from flask_login import login_user, current_user, logout_user, login_required
from werkzeug.utils import secure_filename
import secrets
from PIL import Image
import os
from datetime import datetime as dt

### Συμπληρώστε κάποια από τα imports που έχουν αφαιρεθεί ###

current_year = dt.now().year

### Μέθοδος μετονομασίας και αποθήκευσης εικόνας ###
def image_save(image, where, size):
    random_filename = secrets.token_hex(8)
    file_name, file_extension = os.path.splitext(image.filename)
    image_filename = random_filename + file_extension
    image_path = os.path.join(app.root_path, "static/images/" + where, image_filename)

    image_size = size # this must be a tuple in the form of: (150, 150)
    img = Image.open(image)
    img.thumbnail(image_size)

    img.save(image_path)

    return image_filename


### ERROR HANDLERS START ###
## Εδώ πρέπει να μπεί ο κώδικας για τους error handlers (πχ για το error 404 κλπ).
## Θα πρέπει να φτιαχτούν οι error handlers για τα errors 404, 415 και 500.

@app.errorhandler(404)
def page_not_found(e):
    # note that we set the 404 status explicitly
    return render_template("errors/404.html"), 404

@app.errorhandler(415)
def unsupported_media_type(e):
    # note that we set the 415 status explicitly
    return render_template("errors/415.html"), 415

@app.errorhandler(500)
def internal_server_error(e):
    # note that we set the 500 status explicitly
    return render_template("errors/500.html"), 500

### ERROR HANDLERS END ###

### Αρχική Σελίδα ###
@app.route("/home/")
@app.route("/")
def root():
    ## Να προστεθεί σε αυτό το view ό,τι χρειάζεται για την ταξινόμηση
    ## ανά ημερομηνία εισαγωγής στη βάση, ανά έτος προβολής και ανά rating
    ## με σωστή σελιδοποίηση για την κάθε περίπτωση.

    ## Pagination: page value from "page" parameter from url
    page = request.args.get("page", default = 1, type = int)
    ordering_by = request.args.get("ordering_by", default = "date_created", type = str)

    ## TODO asc or desc
    if(ordering_by == "rating"):
        criterion = Movie.rating.desc()
    elif(ordering_by == "release_year"):
        criterion = Movie.release_year.desc()
    else:
        criterion = Movie.date_created.desc()

    ## Query για ανάσυρση των ταινιών από τη βάση δεδομένων με το σωστό pagination και ταξινόμηση
    movies = Movie.query.order_by(criterion).paginate(per_page = 5, page = page)

    ## Για σωστή ταξινόμηση ίσως πρέπει να περάσετε κάτι επιπλέον μέσα στο context.
    ## Υπενθύμιση: το context είναι το σύνολο των παραμέτρων που περνάμε
    ##             μέσω της render_template μέσα στα templates μας
    ##             στην παρακάτω περίπτωση το context περιέχει μόνο το movies = movies
    return render_template("index.html", movies = movies, ordering_by = ordering_by)


@app.route("/signup/", methods = ["GET", "POST"])
def signup():

    ## Έλεγχος για το αν ο χρήστης έχει κάνει login ώστε αν έχει κάνει,
    ## να μεταφέρεται στην αρχική σελίδα
    if(current_user.is_authenticated):
        return redirect(url_for("root"))

    """Create an instance of the Sign Up form"""
    form = SignupForm()

    if(request.method == "POST" and form.validate_on_submit()):
        ## Να συμπληρωθεί ο κώδικας που φορτώνει τα στοιχεία της φόρμας και τα αποθηκεύει στη βάση δεδομένων
        username = form.username.data
        email = form.email.data
        password = form.password.data
        password2 = form.password2.data

        encrypted_password = bcrypt.generate_password_hash(password).decode("UTF-8")

        user = User(username = username, email = email, password = encrypted_password)
        db.session.add(user)
        db.session.commit()

        flash(f"Ο λογαριασμός για το χρήστη: <b>{username}</b> δημιουργήθηκε με επιτυχία", "success")

        return redirect(url_for("login"))

    return render_template("signup.html", form = form)


## Σελίδα Λογαριασμού Χρήστη με δυνατότητα αλλαγής των στοιχείων του
## Να δοθεί ο σωστός decorator για υποχρεωτικό login
@app.route("/account/", methods = ["GET", "POST"])
@login_required
def account():

    form = AccountUpdateForm(username = current_user.username, email = current_user.email) ## Αρχικοποίηση φόρμας με προσυμπληρωμένα τα στοιχεία του χρήστη

    if(request.method == "POST" and form.validate_on_submit()):

        ## Έλεγχος αν έχει δοθεί νέα εικόνα προφίλ, αλλαγή ανάλυσης της εικόνας
        ## και αποθήκευση στον δίσκο του server και στον χρήστη (δηλαδή στη βάση δεδομένων).
        if(form.profile_image.data):
            try:
                image_file = image_save(form.profile_image.data, "profiles_images", (640, 640))
            except:
                abort(415)
            current_user.profile_image = image_file

        ## Αποθήκευση των υπόλοιπων στοιχείων του χρήστη.
        current_user.username = form.username.data
        current_user.email = form.email.data

        flash(f"Ο λογαριασμός του χρήστη: <b>{current_user.username}</b> ενημερώθηκε με επιτυχία", "success")
        db.session.commit()

        return redirect(url_for("root"))
    else:
        return render_template("account_update.html", form = form)


### Σελίδα Login ###
@app.route("/login/", methods = ["GET", "POST"])
def login():

    ## Έλεγχος για το αν ο χρήστης έχει κάνει login ώστε αν έχει κάνει,
    ## να μεταφέρεται στην αρχική σελίδα
    if(current_user.is_authenticated):
        return redirect(url_for("root"))

    form = LoginForm() ## Αρχικοποίηση φόρμας Login

    if(request.method == "POST" and form.validate_on_submit()):
        email = form.email.data
        password = form.password.data

        ## Ανάκτηση χρήστη από τη βάση με το email του
        ## και έλεγχος του password.
        ## Εάν είναι σωστά, να γίνεται login με τη βοήθεια του Flask-Login
        ## Σε κάθε περίπτωση να εμφανίζονται τα αντίστοιχα flash messages
        user = User.query.filter_by(email = email).first()
        if(user and bcrypt.check_password_hash(user.password, password)):
            flash(f"Επιτυχής είσοδος του χρήστη με email: {email}.", "success")
            login_user(user, remember = form.remember_me.data)

            next_link = request.args.get("next")

            return redirect(next_link) if next_link else redirect(url_for("root"))
        else:
            flash("Είσοδος χρήστη ανεπιτυχής, δοκιμάστε ξανά με τα σωστά email/password.", "warning")


    return render_template("login.html", form = form)


### Σελίδα Logout ###
@app.route("/logout/")
def logout_page():
    ## Αποσύνδεση Χρήστη
    logout_user()
    flash("Έγινε αποσύνδεση του χρήστη.", "success")
    ## Ανακατεύθυνση στην αρχική σελίδα
    return redirect(url_for("root"))




### Σελίδα Εισαγωγής Νέας Ταινίας ###

## Να δοθεί ο σωστός decorator για τη σελίδα με route "new_movie"
## καθώς και ο decorator για υποχρεωτικό login
@app.route("/new_movie/", methods = ["GET", "POST"])
@login_required
def new_movie():
    form = NewMovieForm()

    ## Υλοποίηση της λειτουργίας για ανάκτηση και έλεγχο (validation) των δεδομένων της φόρμας
    ## Τα πεδία που πρέπει να έρχονται είναι τα παρακάτω:
    ## title, plot, image, release_year, rating
    ## Το πεδίο image πρέπει να ελέγχεται αν περιέχει εικόνα και αν ναι
    ## να μετατρέπει την ανάλυσή της σε (640, 640) και να την αποθηκεύει στο δίσκο και τη βάση
    ## αν όχι, να αποθηκεύει τα υπόλοιπα δεδομένα και αντί εικόνας να μπαίνει το default movie image
    if(request.method == "POST" and form.validate_on_submit()):
        title = form.title.data
        plot = form.plot.data
        image = form.image.data
        release_year = form.release_year.data
        rating = form.rating.data

        ## TODO replace with default value in Movie class
        if(form.image.data):
            try:
                image_file = image_save(form.image.data, "movies_images", (640, 640))
            except:
                abort(415)
            movie = Movie(title = title, plot = plot, image = image_file, release_year = release_year, rating = rating, user_id = current_user.id)
        else:
            movie = Movie(title = title, plot = plot, release_year = release_year, rating = rating, user_id = current_user.id)

        db.session.add(movie)
        db.session.commit()

        flash(f"Η ταινία με τίτλο: {title} δημιουργήθηκε με επιτυχία", "success")
        return redirect(url_for("root"))

    return render_template("new_movie.html", form = form, page_title = "Εισαγωγή Νέας Ταινίας", current_year = current_year)


### Πλήρης σελίδα ταινίας ###

## Να δοθεί ο σωστός decorator για τη σελίδα με route "movie"
## και επιπλέον να δέχεται το id της ταινίας ("id")
@app.route("/movie/<int:id>", methods = ["GET"])
def movie(id):

    ## Ανάκτηση της ταινίας με βάση το id
    ## ή εμφάνιση σελίδας 404 page not found
    movie = Movie.query.get_or_404(id)

    return render_template("movie.html", movie = movie)


### Ταινίες ανά χρήστη που τις ανέβασε ###

## Να δοθεί ο σωστός decorator για τη σελίδα με route "movies_by_author"
## και επιπλέον να δέχεται το id του χρήστη ("author_id")
@app.route("/movies_by_author/<int:author_id>")
def movies_by_author(author_id):
    ## Όπως και στην αρχική σελίδα, να προστεθεί σε αυτό το view ότι χρειάζεται για την ταξινόμηση
    ## ανά ημερομηνία εισαγωγής στη βάση, ανά έτος προβολής και ανά rating
    ## με σωστή σελιδοποίηση για την κάθε περίπτωση.

    ## Pagination: page value from "page" parameter from url
    page = request.args.get("page", default = 1, type = int)
    ordering_by = request.args.get("ordering_by", default = "date_created", type = str)

    ## TODO asc or desc
    if(ordering_by == "rating"):
        criterion = Movie.rating.desc()
    elif(ordering_by == "release_year"):
        criterion = Movie.release_year.desc()
    else:
        criterion = Movie.date_created.desc()

    user = User.query.get_or_404(author_id) # Query για ανάσυρση του χρήστη από τη βάση δεδομένων βάσει του id του ("author_id"), ή εμφάνιση σελίδας 404 page not found
    movies = Movie.query.filter_by(author = user).order_by(criterion).paginate(per_page = 3, page = page) ## Query για ανάσυρση των ταινιών βάσει χρήστη από τη βάση δεδομένων με το σωστό pagination και ταξινόμηση

    ## Για σωστή ταξινόμηση ίσως πρέπει να περάσετε κάτι επιπλέον μέσα στο context (εκτός από τον author και τις ταινίες).
    ## Υπενθύμιση: το context είναι το σύνολο των παραμέτρων που περνάμε
    ##             μέσω της render_template μέσα στα templates μας
    ##             στην παρακάτω περίπτωση το context περιέχει τα movies = movies, author = user

    return render_template("movies_by_author.html", movies = movies, author = user, ordering_by = ordering_by)



### Σελίδα Αλλαγής Στοιχείων Ταινίας ###

## Να δοθεί ο σωστός decorator για τη σελίδα με route "edit_movie"
## και επιπλέον να δέχεται το id της ταινίας προς αλλαγή ("id")
## και να προστεθεί και ο decorator για υποχρεωτικό login
@app.route("/edit_movie/<int:id>", methods = ["GET", "POST"])
@login_required
def edit_movie(id):

    ## Ανάκτηση ταινίας βάσει των id, user_id, ή, εμφάνιση σελίδας 404 page not found
    movie = Movie.query.filter_by(id = id, author = current_user).first_or_404()
    ## Έλεγχος αν βρέθηκε η ταινία
    ## αν ναι, αρχικοποίηση της φόρμας ώστε τα πεδία να είναι προσυμπληρωμένα
    ## έλεγχος των πεδίων (validation) και αλλαγή (ή προσθήκη εικόνας) στα στοιχεία της ταινίας
    ## αν δεν βρέθηκε η ταινία, ανακατεύθυνση στην αρχική σελίδα και αντίστοιχο flash μήνυμα στο χρήστη
    ## όπως συζητήθηκε στο forum, η παρούσα μέθοδος μπορεί να υλοποιηθεί και με σφάλμα 404 αντί για ανακατεύθυνση στην αρχική σελίδα

    form = NewMovieForm(title = movie.title, plot = movie.plot, release_year = movie.release_year, rating = movie.rating)

    if(request.method == "POST" and form.validate_on_submit()):
        movie.title = form.title.data
        movie.plot = form.plot.data
        movie.release_year = form.release_year.data
        movie.rating = form.rating.data

        if(form.image.data):
            try:
                image_file = image_save(form.image.data, "movies_images", (640, 640))
            except:
                abort(415)
            movie.image = image_file

        db.session.commit()
        flash(f"Η επεξεργασία της ταινίας έγινε με επιτυχία", "success")
        return redirect(url_for("movie", id = movie.id))

    return render_template("new_movie.html", form = form, movie = movie, page_title = "Αλλαγή Ταινίας")

    ## τα παρακάτω δεν εκτελούνται ποτ΄έ, λόγω του first_or_404
    ##flash(f"Δε βρέθηκε η ταινία", "info")

    ##return redirect(url_for("root"))



### Σελίδα Διαγραφής Ταινίας από τον author της ###

## Να δοθεί ο σωστός decorator για τη σελίδα με route "delete_movie"
## και επιπλέον να δέχεται το id της ταινίας προς αλλαγή ("id")
## και να προστεθεί και ο decorator για υποχρεωτικό login
@app.route("/delete_movie/<int:id>", methods = ["GET", "POST"])
@login_required
def delete_movie(id):
    ## Ανάκτηση ταινίας βάσει των id και author (ο οποίος πρέπει να συμπίπτει με τον current user), ή, εμφάνιση σελίδας 404 page not found
    movie = Movie.query.filter_by(id = id, author = current_user).first_or_404()

    ## Εάν βρεθεί η ταινία, κάνουμε διαγραφή και εμφανίζουμε flash message επιτυχούς διαγραφής
    ## με ανακατεύθυνση στην αρχική σελίδα
    ## αλλιώς εμφανίζουμε flash message ανεπιτυχούς διαγραφής
    ## με ανακατεύθυνση στην αρχική σελίδα
    if movie:
        if(movie.image != "default_movie_image.png"): ## don't delete the default
            try:
                ## adding flaskMoviesApp to the path because this runs from run.py, one dir up
                os.remove(os.path.abspath("./flaskMoviesApp/static/images/movies_images/") + '\\' + movie.image)
            except Exception as e:
                ## print(e)
                pass
        db.session.delete(movie)
        db.session.commit()


        flash("Το άρθρο διεγράφη με επιτυχία.", "success")
        return redirect(url_for("root"))

    ## perhaps redundant ?
    flash("Το άρθρο δεν βρέθηκε.", "warning")
    return redirect(url_for("root"))
