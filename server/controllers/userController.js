import { db } from "../helpers/db.js";

export const getAllUsers = async(req, res) => {
    try{
        const users = db.prepare("SELECT * FROM users").all();

        if (users.length === 0) {
            return res.status(200).json({message: "NO user data found in database!"});
        }
        res.json(users);
    } catch(e) {
        res.status(500).json({message: `Error while fetching user data: ${e.message}!`});
    }
}

export const createUser = async(req, res) => {
    try{
        const {
            username,
            password
        } = req.body;

        const sql = `
            INSERT INTO users
            (username, password) VALUES (?, ?)
        `;
        const stmt = db.prepare(sql);
        stmt.run(username, password);

        res.json({message: "New user added."});
    } catch(e) {
        res.status(500).json({message: `Error while creating new user: ${e.message}!`})
    }
}