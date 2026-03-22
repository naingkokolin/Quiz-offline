import express from "express"
import { getAllUsers, createUser, loginUser, changePassword } from "../controllers/userController.js"

const router = express.Router();

router.get("/getUsers", getAllUsers);
router.post("/create-user", createUser);
router.post("/login", loginUser);
router.post("/change-password", changePassword);

export default router;