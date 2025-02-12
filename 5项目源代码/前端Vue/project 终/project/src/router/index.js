import Vue from 'vue'
import VueRouter from 'vue-router'
import Register from '../views/Register.vue'

// 学生端
import Student from '../views/student/Student.vue'
import StudentSchedule from '../views/student/StudentSchedule.vue'
import CourseSelection from '../views/student/CourseSelection.vue'
import SelectedCourses from '../views/student/SelectedCourses.vue'
import StudentGrade from '../views/student/StudentGrade.vue'
import StudentInformation from '../views/student/StudentInformation.vue'

// 教师端
import Teacher from '../views/teacher/Teacher.vue'
import TeacherInformation from '../views/teacher/TeacherInformation.vue'
import TeacherCourses from '../views/teacher/TeacherCourses.vue'
import GradeView from '../views/teacher/GradeView.vue'
import GradeModification from '../views/teacher/GradeModification.vue'

// 管理员端
import Admin from '../views/admin/Admin.vue'
import TeacherManagement from '../views/admin/TeacherManagement.vue'
import StudentManagement from '../views/admin/StudentManagement.vue'
import CourseManagement from '../views/admin/CourseManagement.vue'
import CourseSelectedManagement from '../views/admin/CourseSelectedManagement.vue'
import GradeManagement from '../views/admin/GradeManagement.vue'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/Login.vue')  //两种写法
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/student',
    redirect: '/student/studentSchedule',
    component: Student,
    children: [
      {
        path: 'studentSchedule',
        component: StudentSchedule
      },
      {
        path: 'courseSelection',
        component: CourseSelection
      },
      {
        path: 'selectedCourses',
        component: SelectedCourses
      },
      {
        path: 'studentGrade',
        component: StudentGrade
      },
      {
        path: 'studentInformation',
        component: StudentInformation
      }
    ]
  },
  {
    path: '/teacher',
    redirect: '/teacher/teacherInformation',
    component: Teacher,
    children: [
      {
        path: 'teacherInformation',
        component: TeacherInformation
      },
      {
        path: 'teacherCourses',
        component: TeacherCourses
      },
      {
        path: 'gradeView',
        component: GradeView
      },
      {
        path: 'gradeModification',
        component: GradeModification
      }
    ]
  },
  {
    path: '/admin',
    redirect: '/admin/teacherManagement',
    component: Admin,
    children: [
      {
        path: 'teacherManagement',
        component: TeacherManagement
      },
      {
        path: 'studentManagement',
        component: StudentManagement
      },
      {
        path: 'courseManagement',
        component: CourseManagement
      },
      {
        path: 'courseSelectedManagement',
        component: CourseSelectedManagement
      },
      {
        path: 'gradeManagement',
        component: GradeManagement
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
