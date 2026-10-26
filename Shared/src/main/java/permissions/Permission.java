package permissions;

public enum Permission {
    BUY_NOTES,                  // Students can buy notes
    SELL_NOTES,                 // Students can sell notes
    BROWSE_LISTINGS,            // Students can browse the notes that other students are selling
    JOIN_CLASSES,               // Students can join teachers' classes
    UPLOAD_NOTES,               // Students can upload their notes (not for selling)
    REPORT_NOTES,               // Students can report stolen notes and fake notes

    CREATE_CLASSES,             // Teachers can create classes to share their notes with students
    UPLOAD_NOTES_IN_CLASSES,    // Teachers can share their notes with students

    VIEW_REPORTED_NOTES,        // Moderators can view all the reported notes
    VIEW_ALL_NOTES,             // Moderators can view all notes in the listings without buying them
    DELETE_NOTES,               // Moderators can delete notes in the listings
}
