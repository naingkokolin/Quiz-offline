package org.mst_college.project.quiz.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Question
import utils.QuestionManager
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun JudgePanel(onBack: () -> Unit) {
    var category by remember { mutableStateOf("") }
    var questionText by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctAns by remember { mutableStateOf("A") }

    var editingQuestion by remember { mutableStateOf<Question?>(null) }
    val allQuestions = remember { mutableStateListOf<Question>() }

    var showOnlyUsed by remember { mutableStateOf(false) }

    fun refreshQuestions() {
        allQuestions.clear()
        allQuestions.addAll(QuestionManager.getAllQuestions())
    }

    LaunchedEffect(Unit) { refreshQuestions() }

    fun clearForm() {
        editingQuestion = null
        category = ""; questionText = ""; optionA = ""; optionB = ""; optionC = ""; optionD = ""; correctAns = "A"
    }

    val mainGradient = Brush.linearGradient(listOf(Color(0xFFF0F4F8), Color(0xFFD9E2EC)))

    Row(modifier = Modifier.fillMaxSize().background(mainGradient)) {

        // --- LEFT SIDE: INPUT FORM ---
        Box(
            modifier = Modifier
                .weight(0.42f)
                .fillMaxHeight()
                .padding(25.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(15.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                backgroundColor = Color.White,
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(28.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp).clickable { onBack() },
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF102A43).copy(alpha = 0.05f)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(Modifier.width(15.dp))
                        Text(
                            if (editingQuestion == null) "New Question" else "Update Content",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF102A43)
                        )
                    }

                    Divider(Modifier.padding(vertical = 20.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    AdminTextField("Category", category, Icons.Default.Category) { category = it }
                    AdminTextField("Question", questionText, Icons.Default.Description, isMultiLine = true) { questionText = it }

                    Text("Answer Options", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF486581))
                    Spacer(Modifier.height(12.dp))

                    Row {
                        Column(Modifier.weight(1f)) {
                            OptionField("A", optionA) { optionA = it }
                            OptionField("C", optionC) { optionC = it }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            OptionField("B", optionB) { optionB = it }
                            OptionField("D", optionD) { optionD = it }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Text("Set Correct Key", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF486581))
                    CorrectAnswerDropdown(correctAns) { correctAns = it }

                    Spacer(Modifier.height(35.dp))

                    val btnColor = if (editingQuestion == null) Color(0xFF243B55) else Color(0xFFF49124)
                    Button(
                        onClick = {
                            if (questionText.isNotBlank()) {
                                val q = Question(category, questionText, optionA, optionB, optionC, optionD, correctAns)
                                if (editingQuestion != null) QuestionManager.deleteQuestion(editingQuestion!!)
                                QuestionManager.saveQuestion(q)
                                refreshQuestions()
                                clearForm()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(58.dp).clip(RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp)
                    ) {
                        Icon(if (editingQuestion == null) Icons.Default.AddCircle else Icons.Default.Update, null, tint = Color.White)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (editingQuestion == null) "SAVE" else "APPLY CHANGES",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    if (editingQuestion != null) {
                        TextButton(onClick = { clearForm() }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                            Text("Discard Changes", color = Color.Red.copy(alpha = 0.7f))
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                }
            }
        }

        // --- RIGHT SIDE: QUESTION Management ---
        Column(modifier = Modifier.weight(0.58f).padding(top = 25.dp, end = 25.dp, bottom = 25.dp)) {

            // Header Row: Title နဲ့ Import Button ကို တစ်တန်းတည်းထားမယ်
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Questions", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF102A43))
                    Text("${allQuestions.size} questions in Database", color = Color(0xFF627D98))
                }

                // Import Button ကို ညာဘက်မှာ ထားမယ်
                Button(
                    onClick = { pickJsonFile()?.let { QuestionManager.importQuestionsFromFile(it); refreshQuestions() } },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier.height(45.dp)
                ) {
                    Icon(Icons.Default.FileUpload, null, tint = Color(0xFF243B55))
                    Text(" IMPORT JSON", fontWeight = FontWeight.Bold, color = Color(0xFF243B55))
                }
            }

            Spacer(Modifier.height(20.dp))

            // Tab Selector: Available နဲ့ Used ခွဲဖို့ (Title ရဲ့ အောက်မှာ သီးသန့် Row တစ်ခုအနေနဲ့ ထားမယ်)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {

                val availableCount = allQuestions.count { !it.isUsed }
                val usedCount = allQuestions.count { it.isUsed }

                // Available Tab
                TextButton(
                    onClick = { showOnlyUsed = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = if (!showOnlyUsed) Color(0xFF243B55) else Color.Gray)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("AVAILABLE ($availableCount)", fontWeight = if (!showOnlyUsed) FontWeight.Bold else FontWeight.Normal)
                        if (!showOnlyUsed) Box(Modifier.height(3.dp).width(60.dp).background(Color(0xFF243B55), RoundedCornerShape(2.dp)))
                    }
                }

                Spacer(Modifier.width(24.dp))

                // Used Tab
                TextButton(
                    onClick = { showOnlyUsed = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = if (showOnlyUsed) Color(0xFF243B55) else Color.Gray)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("USED QUESTIONS ($usedCount)", fontWeight = if (showOnlyUsed) FontWeight.Bold else FontWeight.Normal)
                        if (showOnlyUsed) Box(Modifier.height(3.dp).width(60.dp).background(Color(0xFF243B55), RoundedCornerShape(2.dp)))
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            // Questions List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                val displayList = if (showOnlyUsed) {
                    allQuestions.filter { it.isUsed }
                } else {
                    allQuestions.filter { !it.isUsed }
                }

                itemsIndexed(displayList) { index, q ->
                    AdminQuestionItem(
                        index = index + 1,
                        q = q,
                        onEdit = {
                            editingQuestion = q
                            category = q.category
                            questionText = q.question
                            optionA = q.option_a
                            optionB = q.option_b
                            optionC = q.option_c
                            optionD = q.option_d
                            correctAns = q.correct_answer
                        },
                        onDelete = { QuestionManager.deleteQuestion(q); refreshQuestions() },
                        onRestore = {
                            QuestionManager.removeFromUsed(q)
                            refreshQuestions()
                        }
                    )
                }
            }
        }
    }
}

// --- Helper Components ---
@Composable

fun OptionField(label: String, value: String, onValueChange: (String) -> Unit) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Option $label") },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true
    )
}

fun pickJsonFile(): File? {
    val fileDialog = FileDialog(null as Frame?, "Select Questions JSON", FileDialog.LOAD)
    fileDialog.isVisible = true
    val directory = fileDialog.directory
    val fileName = fileDialog.file
    return if (directory != null && fileName != null) File(directory, fileName) else null
}

@Composable
fun AdminTextField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isMultiLine: Boolean = false, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 18.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(icon, null, tint = Color(0xFF486581), modifier = Modifier.size(20.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF243B55),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                backgroundColor = Color(0xFFF0F4F8).copy(alpha = 0.3f)
            ),
            maxLines = if (isMultiLine) 4 else 1
        )
    }
}

@Composable
fun CorrectAnswerDropdown(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.padding(top = 10.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(55.dp).clickable { expanded = true },
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            color = Color(0xFFF0F4F8).copy(alpha = 0.3f)
        ) {
            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Correct Answer Key: ", color = Color.Gray)
                Text(selected, fontWeight = FontWeight.Bold, color = Color(0xFF243B55))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, null)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("A", "B", "C", "D").forEach { opt ->
                DropdownMenuItem(onClick = { onSelect(opt); expanded = false }) { Text(opt) }
            }
        }
    }
}

@Composable
fun AdminQuestionItem(index: Int, q: Question, onEdit: () -> Unit, onDelete: () -> Unit, onRestore: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (q.isUsed) Color(0xFFF8F9FA) else Color.White
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .size(55.dp) // size ကို နည်းနည်းလေး ပိုကြီးပေးရင် ပိုကြည့်ကောင်းတယ်
                    .background(Color(0xFF243B55).copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("#$index", fontSize = 12.sp, color = Color.Gray)
                Text(q.correct_answer, fontWeight = FontWeight.Black, color = Color(0xFF243B55), fontSize = 18.sp)
            }
            Spacer(Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(q.category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF627D98), letterSpacing = 1.sp)
                Text(q.question, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 16.sp, color = Color(0xFF102A43))
            }
            if (q.isUsed) {
                // Used ဖြစ်နေမှ ပေါ်မယ့် Restore Button (Used ထဲကထုတ်မယ်)
                IconButton(onClick = onRestore) {
                    Icon(Icons.Default.SettingsBackupRestore, "Restore", tint = Color(0xFF4CAF50))
                }
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Edit", tint = Color(0xFF2196F3)) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFEF5350)) }
        }
    }
}