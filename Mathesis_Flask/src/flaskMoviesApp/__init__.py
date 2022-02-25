from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from flask_login import LoginManager

app = Flask(__name__)

### Configuration για τα Secret Key, WTF CSRF Secret Key, SQLAlchemy Database URL,
## Το όνομα του αρχείου της βάσης δεδομένων θα πρέπει να είναι 'flask_movies_database.db'

## using secrets.token_hex(16)
app.config["SECRET_KEY"] = "96fdb43f231194a90fac710e5f88455f"
app.config['WTF_CSRF_SECRET_KEY'] = "11c948b1f966c6a4d21c0397d13ea5b2"
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///flask_movies_database.db"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

### Αρχικοποίηση της Βάσης, και άλλων εργαλείων ###
### Δώστε τις σωστές τιμές παρακάτω ###

db = SQLAlchemy(app)

bcrypt = Bcrypt(app)

login_manager = LoginManager(app)

login_manager.login_view = "login"
login_manager.login_message_category = "primary" ## θα μπορούσε να είναι warning, στο βίντεο της εκφώνησης το μήνυμα έχει κυανό χρώμα
login_manager.login_message = "Παρακαλούμε κάνετε login για να μπορέσετε να δείτε αυτήν την σελίδα."

db.create_all()

from flaskMoviesApp import routes, models
