import { createRouter, createWebHistory } from "vue-router"
import store from "@/store"

import Home from "@/views/Home.vue"
import AuthLayout from "@/layouts/Auth.vue"
import ProfileLayout from "@/layouts/Profile.vue"
import ProjectLayout from "@/layouts/Project.vue"
import ChatLayout from "@/layouts/Chat.vue"
import StudyLayout from "@/layouts/Study.vue"
import StudyBoardLayout from "@/layouts/StudyBoard.vue"

import Profile from "@/views/Profile.vue"
import EditProfile from "@/views/EditProfile.vue"

import ProjectList from "@/views/project/ProjectList.vue"
import ProjectForm from "@/views/project/ProjectForm.vue"
import ProjectEditForm from "@/views/project/ProjectEditForm.vue"
import ProjectArticle from "@/views/project/ProjectArticle.vue"
import ProjectDetail from "@/views/project/ProjectDetail.vue"
import ProjectManage from "@/views/project/detail/manage/ProjectManage.vue"
import ProjectBoard from "@/views/project/detail/board/ProjectBoard.vue"
import ProjectNotiBoard from "@/views/project/detail/notiboard/ProjectNotiBoard.vue"
import ArticleForm from "@/views/project/detail/articleform/ArticleForm.vue"
import ArticleEditForm from "@/views/project/detail/articleform/ArticleEditForm.vue"
import BoardArticleList from "@/views/project/detail/board/BoardArticleList.vue"
import ArticleDetail from "@/views/project/detail/ArticleDetail.vue"
import NotiBoardArticleList from "@/views/project/detail/notiboard/NotiBoardArticleList.vue"

// Study
import StudyPage from "@/views/study/index"

import Login from "@/views/auth/Login.vue"
import Signup from "@/views/auth/Signup.vue"
import FindPassword from "@/views/auth/FindPassword.vue"

import ChatList from "@/views/chat/ChatList.vue"
import ChatDetail from "@/views/chat/ChatDetail.vue"

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/auth",
    component: AuthLayout,
    meta: { requiresNoAuth: true },
    children: [
      {
        path: "login",
        name: "Login",
        component: Login,
      },
      {
        path: "signup",
        name: "Signup",
        component: Signup,
      },
      {
        path: "find-password",
        name: "FindPassword",
        component: FindPassword,
      },
    ],
  },
  {
    path: "/profile",
    component: ProfileLayout,
    children: [
      {
        path: ":email",
        name: "Profile",
        component: Profile,
        props: true,
      },
      {
        path: "edit",
        name: "EditProfile",
        component: EditProfile,
      },
    ],
  },
  {
    path: "/study",
    component: StudyLayout,
    children: [
      {
        path: "",
        name: "StudyList",
        component: StudyPage.StudyList,
      },
      {
        path: "create",
        name: "StudyCreate",
        component: StudyPage.StudyCreate,
      },
      {
        path: ":studyId/article",
        name: "StudyArticle",
        component: StudyPage.StudyArticle,
        props: true,
      },
      {
        path: ":studyId/edit",
        name: "StudyEdit",
        component: StudyPage.StudyEdit,
        props: true,
      },
      {
        path: ":studyId/board",
        component: StudyBoardLayout,
        props: true,
        children: [
          {
            path: "create",
            name: "StudyBoardCreate",
            component: StudyPage.StudyBoardCreate,
            props: true,
          },
          {
            path: ":boardId/edit",
            name: "StudyBoardEdit",
            component: StudyPage.StudyBoardEdit,
            props: true,
          },
          {
            path: "articles/create",
            name: "StudyBoardArticleCreate",
            component: StudyPage.StudyBoardArticleCreate,
            props: true,
          },
          {
            path: ":boardId/articles",
            name: "StudyBoardArticleList",
            component: StudyPage.StudyBoardArticleList,
            props: true,
          },
          {
            path: ":boardId/articles/:articleId",
            name: "StudyBoardArticleDetail",
            component: StudyPage.StudyBoardArticleDetail,
            props: true,
          },
          {
            path: ":boardId/articles/:articleId/edit",
            name: "StudyBoardArticleEdit",
            component: StudyPage.StudyBoardArticleEdit,
            props: true,
          },
          {
            path: "manage",
            name: "StudyManage",
            component: StudyPage.StudyManage,
            props: true,
          },
        ],
      },
    ],
  },
  {
    path: "/projects",
    component: ProjectLayout,
    children: [
      {
        path: "",
        name: "ProjectList",
        component: ProjectList,
      },
      {
        path: "article/form",
        name: "ProjectCreateForm",
        component: ProjectForm,
      },
      {
        path: "article/:projectId",
        name: "ProjectArticle",
        component: ProjectArticle,
      },
      {
        path: "article/:projectId/form",
        name: "ProjectEditForm",
        component: ProjectEditForm,
      },
      {
        path: "detail/:projectId",
        name: "ProjectDetail",
        component: ProjectDetail,
        children: [
          {
            path: "manage",
            name: "ProjectManage",
            component: ProjectManage,
          },
          {
            path: "board/:boardId",
            component: ProjectBoard,
            children: [
              {
                path: "",
                name: "BoardArticleList",
                component: BoardArticleList,
              },
              {
                path: "detail/:articleId",
                name: "ArticleDetail",
                component: ArticleDetail,
              },
            ],
          },
          {
            path: "noti-board/:boardId",
            component: ProjectNotiBoard,
            children: [
              {
                path: "",
                name: "NotiBoardArticleList",
                component: NotiBoardArticleList,
              },
              {
                path: "detail/:articleId",
                name: "ArticleDetail",
                component: ArticleDetail,
              },
            ],
          },
          {
            path: "article/form",
            name: "ArticleForm",
            component: ArticleForm,
          },
          {
            path: "article/:articleId/form",
            name: "ArticleEditForm",
            component: ArticleEditForm,
          },
        ],
      },
    ],
  },
  {
    path: "/chat",
    name: "Chat",
    component: ChatLayout,
    children: [
      {
        path: "chatlist",
        name: "ChatList",
        component: ChatList,
      },
      {
        path: ":email",
        name: "ChatDetail",
        component: ChatDetail,
      },
    ],
  },
  {
    path: "/:notFound(.*)",
    name: "NotFound",
    redirect: () => {
      return { name: "Home" }
    },
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  // 로그인 상태에서 접근할 수 없는 페이지
  if (to?.meta?.requiresNoAuth) {
    if (store.getters["auth/getIsAuthenticated"]) {
      alert("여기는 오면 안돼")
      next({ name: "Home" })
      return
    }
  }
  next()
})

export default router
