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

export const createUser = async(req, res) => {
    try{
        const {
            username,
            password
        } = req.body;

        const hashedPassword = await bcrypt.hash(password, 10);

        const sql = `
            INSERT INTO users
            (username, password) VALUES (?, ?)
        `;
        const stmt = db.prepare(sql);
        stmt.run(username, hashedPassword);

        res.json({message: "New user added."});
    } catch(e) {
        res.status(500).json({message: `Error while creating new user: ${e.message}!`})
    }
}

export const loginUser = async (req, res) => {
    try {
        const { username, password } = req.body;

        // 1. User ရှိမရှိ အရင်ရှာမယ်
        const user = db.prepare("SELECT * FROM users WHERE username = ?").get(username);

        if (!user) {
            return res.status(401).json({ message: "Invalid username or password!" });
        }

        // 2. Password ကို တိုက်စစ်မယ် (Bcrypt သုံးထားရင်)
        // မှတ်ချက်: မင်း အရင်က plain text နဲ့ သိမ်းထားရင် bcrypt.compare သုံးလို့မရသေးဘူး
        const isMatch = await bcrypt.compare(password, user.password);

        if (!isMatch) {
            return res.status(401).json({ message: "Invalid username or password!" });
        }

        res.json({ message: "Login successful!", userId: user.id });
    } catch (e) {
        res.status(500).json({ message: `Login error: ${e.message}` });
    }
}

// CHANGE PASSWORD
export const changePassword = async (req, res) => {
    try {
        const { userId, oldPassword, newPassword } = req.body;

        // 1. User ကို ရှာမယ်
        const user = db.prepare("SELECT * FROM users WHERE user_id = ?").get(userId);
        if (!user) return res.status(404).json({ message: "User not found!" });

        // 2. Old password မှန်မမှန် စစ်မယ်
        const isMatch = await bcrypt.compare(oldPassword, user.password);
        if (!isMatch) return res.status(400).json({ message: "Current password incorrect!" });

        // 3. New password ကို hash လုပ်ပြီး update လုပ်မယ်
        const hashedNewPassword = await bcrypt.hash(newPassword, 10);
        db.prepare("UPDATE users SET password = ? WHERE user_id = ?").run(hashedNewPassword, userId);

        res.json({ message: "Password updated successfully!" });
    } catch (e) {
        res.status(500).json({ message: `Update error: ${e.message}` });
    }
}