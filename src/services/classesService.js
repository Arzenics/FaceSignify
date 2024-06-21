const { db, admin } = require('../config/firebaseConfig');
const { v4: uuidv4 } = require('uuid');
const InputError = require('../exceptions/InputError');
const {predict} = require ('./faceRecognitionService');

const createClass = async (studentId, teacherUsername, subject,className, prediction, publicUrl) => {
  try {
    // Ambil data student dari collection users
    const studentPromises = studentId.map(async (studentId) => {
      const studentRef = db.collection('users').doc(studentId);
      const studentDoc = await studentRef.get();
      if (!studentDoc.exists) {
        throw new InputError(`Student dengan ID ${studentId} tidak ditemukan`);
      }
      const studentData = studentDoc.data();
      return { username: studentData.username, email: studentData.email };
    });

    const students = await Promise.all(studentPromises);

    // Ambil data teacher dari collection admins
    const teacherRef = db.collection('admins').doc(teacherUsername);
    const teacherDoc = await teacherRef.get();
    if (!teacherDoc.exists) {
      throw new InputError('Teacher with the specified username does not exist');
    }

    // Buat classCode secara otomatis menggunakan uuid
    const classCode = uuidv4().slice(0,5);

    // Simpan kelas ke Firestore
    const classData = {
      students,
      teacherUsername,
      subject,
      classCode,
      className,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
    };

    await db.collection('classes').doc(classCode).set(classData);

    return { classCode };
  } catch (error) {
    console.error('Error creating class:', error);
    throw new Error('Failed to create class: ' + error.message);
  }
};

const joinClass = async (classCode, studentId) => {
  try {
    const classRef = db.collection('classes').doc(classCode);
    const classDoc = await classRef.get();

    if (!classDoc.exists) {
      throw new Error(`Class with code ${classCode} not found`);
    }

   
    // Update array of students in the class document using admin.firestore.FieldValue.arrayUnion
    await classRef.update({
      students: admin.firestore.FieldValue.arrayUnion(studentId),
    });


    console.log(`Student ${studentId} joined class ${classCode} successfully.`);
  } catch (error) {
    console.error('Error joining class:', error);
    throw new Error('Failed to join class: ' + error.message);
  }
};


const getClasses = async () => {
  const snapshot = await db.collection('classes').get();
  const classes = snapshot.docs.map(doc => ({
    id: doc.id,
    ...doc.data(),
  }));
  return classes;
};

const getClassDetails = async ({ classCode, className, subject }) => {
  const classesRef = db.collection('classes');
  const snapshot = await classesRef.where('classCode', '==', classCode)
                                   .where('className', '==', className)
                                   .where('subject', '==', subject)
                                   .get();

  if (snapshot.empty) {
    throw new Error('No matching classes found');
  }

  let classDetails = [];
  snapshot.forEach(doc => {
    classDetails.push(doc.data());
  });

  return classDetails;
};

const addPredictionToClass = async (classCode, prediction, publicUrl) => {
  try {
    const classRef = db.collection('classes').doc(classCode);
    const classDoc = await classRef.get();

    if (!classDoc.exists) {
      throw new Error(`Class with code ${classCode} not found`);
    }

    const classData = classDoc.data();
    const updatedPredictions = classData.predictions ? [...classData.predictions, ...prediction] : prediction;
    const updatedImages = classData.images ? [...classData.images, publicUrl] : [publicUrl];

    await classRef.update({
      predictions: updatedPredictions,
      images: updatedImages,
    });

    console.log('Prediction added to class successfully.');
  } catch (error) {
    console.error('Error adding prediction to class:', error);
    throw new Error('Failed to add prediction to class: ' + error.message);
  }
};
module.exports = { createClass,joinClass, getClasses, getClassDetails, addPredictionToClass };
