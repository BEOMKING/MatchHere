// import axios from "axios"
import ProjectAPI from "@/api/project"

export default {
  namespaced: true,
  state: {
    myClubList: [],
    acceptedApplyers: [],
  },
  mutations: {
    SET_MY_CLUB_LIST(state, myClubList) {
      state.myClubList = myClubList
    },
    ADD_MEMBER(state, acceptedApplyer) {
      console.log("add member")
      acceptedApplyer.authority = "팀원"
      acceptedApplyer.id = acceptedApplyer.memberId
      state.acceptedApplyers.push(acceptedApplyer)
      console.log(state.acceptedApplyers)
    },
  },
  actions: {
    async getMyClubList({ commit }) {
      const { clubs } = await ProjectAPI.getMyClubList()
      console.log("my clubs: ", clubs)
      commit("SET_MY_CLUB_LIST", clubs)
    },
    async createProject(context, formData) {
      try {
        const resData = await ProjectAPI.createProject(formData)
        const id = resData.id
        console.log(id)
        return id
      } catch (error) {
        console.log(error.response)
        const { status } = error.response
        throw Error(`임시: ${status} 문제가 발생했어!`)
      }
    },
    async updateProject(context, { formData, projectId }) {
      try {
        console.log(projectId)
        const resData = await ProjectAPI.updateProject(formData, projectId)
        return resData
      } catch (error) {
        console.log(error.response)
        const { status } = error.response
        throw Error(`임시: ${status} 문제가 발생했어!`)
      }
    },
    async getProject(context, projectId) {
      const projectInfo = await ProjectAPI.getProject(projectId)
      // console.log(projectInfo)
      return projectInfo
    },
    async getInfoForUpdate(context, projectId) {
      try {
        const projectInfo = await ProjectAPI.getInfoForUpdate(projectId)
        return projectInfo
      } catch (error) {
        const { status, message } = error.response.data
        console.log(error.response)
        switch (status) {
          case 401:
            throw Error(message)
        }
      }
    },
    async applyForParticipation(context, { reqForm, projectId }) {
      try {
        const resData = await ProjectAPI.projectApply(reqForm, projectId)
        return resData
      } catch (error) {
        console.log(error.response)
        const { status, message } = error.response.data
        switch (status) {
          case 400:
            throw Error(message)
          default:
            throw Error(`임시: ${status} 문제가 발생했어~`)
        }
      }
    },
    async getAllApplication(context, projectId) {
      try {
        const resData = await ProjectAPI.getAllApplication(projectId)
        return resData
      } catch (error) {
        console.log(error.response)
        const { status } = error.response
        throw Error(`임시: ${status} 문제가 발생했어~`)
      }
    },
    async getApplication(context, { projectId, memberId }) {
      try {
        const resData = await ProjectAPI.getApplication(projectId, memberId)
        return resData
      } catch (error) {
        console.log(error.response)
        throw Error(`임시 문제 발생`)
      }
    },
    async getProjectMemberList(context, projectId) {
      try {
        const resData = await ProjectAPI.getProjectMemberList(projectId)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async acceptApplication({ commit }, { projectId, applyer }) {
      try {
        console.log(projectId, applyer)
        const resData = await ProjectAPI.acceptApplication(
          projectId,
          applyer.memberId
        )
        commit("ADD_MEMBER", applyer)
        return resData
      } catch (error) {
        const { status, message } = error.response.data
        switch (status) {
          case 400: {
            throw Error(message)
          }
        }
      }
    },
    async refuseApplication(context, { projectId, memberId }) {
      try {
        const resData = await ProjectAPI.refuseApplication(projectId, memberId)
        return resData
      } catch (error) {
        console.log(error.resopnse)
      }
    },
    async getBoardList(context, projectId) {
      try {
        const resData = await ProjectAPI.getBoardList(projectId)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async getBoardArticleList(context, boardId) {
      try {
        const resData = await ProjectAPI.getBoardArticleList(boardId)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async createBoardArticle(context, reqForm) {
      try {
        const resData = await ProjectAPI.createBoardArticle(reqForm)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async updateBoardArticle(context, { reqForm, articleId }) {
      console.log(reqForm)
      console.log(articleId)
      try {
        const resData = await ProjectAPI.updateBoardArticle(reqForm, articleId)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async deleteBoardArticle(context, articleId) {
      try {
        const resData = await ProjectAPI.deleteBoardArticle(articleId)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async getBoardArticleDetail(context, articleId) {
      try {
        const resData = await ProjectAPI.getBoardArticleDetail(articleId)
        return resData
      } catch (error) {
        console.log(error.response)
        const { status, message } = error.response.data
        console.log(status)
        throw Error(message)
      }
    },
    async getArticleComment(context, articleId) {
      try {
        const resData = await ProjectAPI.getArticleComment(articleId)
        return resData
      } catch (error) {
        console.log(error.response)
        const { status, message } = error.response.data
        console.log(status)
        throw Error(message)
      }
    },
    async createComment(context, { content, articleId, parentId }) {
      try {
        console.log(content, articleId, parentId)
        const resData = await ProjectAPI.createComment(
          content,
          articleId,
          parentId
        )
        return resData
      } catch (error) {
        console.log(error.response)
        const { status, message } = error.response.data
        console.log(status)
        throw Error(message)
      }
    },
    async getAuthority(context, projectId) {
      try {
        const resData = await ProjectAPI.getAuthority(projectId)
        return resData
      } catch (error) {
        console.log(error.response)
      }
    },
    async changeAuthority(context, { projectId, memberId, authority }) {
      console.log(projectId, memberId, authority)
      try {
        const resData = await ProjectAPI.changeAuthority(
          projectId,
          memberId,
          authority
        )
        return resData
      } catch (error) {
        console.log(error.response)
        const { status, message } = error.response.data
        console.log(status)
        throw Error(message)
      }
    },
    async changeRole(context, { projectId, memberId, role }) {
      console.log(projectId, memberId, role)
      try {
        const resData = await ProjectAPI.changeRole(projectId, memberId, role)
        return resData
      } catch (error) {
        console.log(error.response)
        const { status, message } = error.response.data
        console.log(status)
        throw Error(message)
      }
    },
    async updatePicture(context, { projectId, uuid }) {
      console.log(projectId, uuid)
      try {
        const resData = await ProjectAPI.updatePicture(projectId, uuid)
        return resData
      } catch (error) {
        console.log(error.response)
        throw Error("사진 변경에 실패했습니다")
      }
    },
    async viewCount(context, projectId) {
      try {
        console.log("조회수 증가 요청")
        const resData = await ProjectAPI.viewCount(projectId)
        console.log(resData)
        console.log("증가 성공")
      } catch (error) {
        console.log(error.response)
        console.log("증가 실패")
      }
    },
  },
  getters: {
    getMyClubList(state) {
      return state.myClubList
    },
    getAcceptedApplyers(state) {
      return state.acceptedApplyers
    },
  },
}
