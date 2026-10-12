import 'dotenv/config'
import { Sequelize } from 'sequelize';

/* ΤΟ ΚΑΤΩΤΕΡΩ ΤΜΗΜΑ ΕΙΝΑΙ ΓΙΑ ΤΟ DEPLOYMENT ΣΤΟ CLOUD
const sequelize = new Sequelize(process.env.DATABASE_URL, {
    dialect: 'postgres',
    dialectOptions: {
        ssl: {
            require: true,
            rejectUnauthorized: false
        }
    },
    logging: false,
    define: {
        timestamps: false,
        freezeTableName: true
    }
});

*/
const sequelize = new Sequelize({
    host: process.env.POSTGRES_HOST,
    port: 5432,
    database: 'postgres',
    schema: 'booklist',
    dialect: 'postgres',
    username: process.env.POSTGRES_USERNAME,
    password: process.env.POSTGRES_PASSWORD,
    logging: false,
    define: {
        timestamps: false,
        freezeTableName: true
    }
});

export default sequelize
