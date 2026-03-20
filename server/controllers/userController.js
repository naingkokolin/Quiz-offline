import { db } from "../helpers/db.js";
import bcrypt from "bcrypt";

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

export const register = async(req, res) => {
    try{
        const {
            username,
            password
        } = req.body;

        const salt = 10;
        const hashedPassword = await bcrypt.hash(password, salt);

        const sql = `
            INSERT INTO users
            (username, password) VALUES (?, ?)
        `;
        const stmt = db.prepare(sql);
        stmt.run(username, hashedPassword);

        res.json({message: "User registered successfully."});
    } catch(e) {
        res.status(500).json({message: `Error while creating new user: ${e.message}!`})
    }
}

export const login = async (req, res) => {
    const { username, password } = req.body;

    const sql = `
        SELECT password from users WHERE username = ?
    `;

}