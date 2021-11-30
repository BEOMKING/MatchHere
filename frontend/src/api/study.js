import http from "./http"

const createStudy = async (data) => {
  try {
    await http.post("/study", data)
    return {
      text: "스터디를 생성했습니다",
      type: "success",
    }
  } catch (error) {
    console.dir(error)
    switch (error.response?.status) {
      case 400: {
        return {
          text: error.response?.data?.message,
          type: "error",
        }
      }
      case 404: {
        return {
          text: error.response?.data?.message,
          type: "error",
        }
      }
    }
  }
}

const editStudy = async ({ data, studyId }) => {
  await http.put(`/study/${studyId}`, data)
}

const getStudyList = async () => {
  try {
    const res = await http.get("/study")
    return Promise.resolve({
      data: res.data,
      type: "success",
    })
  } catch (error) {
    return Promise.reject({
      text:
        error.response?.data?.message || "스터디 목록을 불러오지 못했습니다",
      type: "error",
    })
  }
}

const getStudy = async (studyId) => {
  try {
    const res = await http.get(`study/${studyId}`)
    return Promise.resolve({
      data: res.data,
      type: "success",
    })
  } catch (error) {
    return Promise.reject({
      text: error.response?.data?.message || "스터디를 불러오지 못했습니다",
      type: "error",
    })
  }
}

const updateViewCountOfStudyArticle = async (studyId) => {
  try {
    await http.put(`study/view-count/${studyId}`)
  } catch (error) {
    console.log(error)
  }
}

const submitApplication = async ({ bio, studyId }) => {
  try {
    await http.post(`studyapplication/${studyId}`, { bio })
  } catch (error) {
    console.log(error)
  }
}

const getStudyBoards = async (studyId) => {
  try {
    const res = await http.get(`study/${studyId}/boards`)
    return res.data
  } catch (error) {
    console.log(error)
  }
}

// BoardArticle
const createBoardArticle = async (data) => {
  try {
    await http.post(`studyboards`, data)
  } catch (error) {
    console.log(error)
  }
}

const getBoardArticles = async (boardId) => {
  try {
    const res = await http.get(`studyboards/${boardId}/articles`)
    return res.data
  } catch (error) {
    console.log(error)
  }
}

const updateBoardArticle = async ({ article, articleId }) => {
  try {
    const res = await http.put(`studyboards/${articleId}`, article)
    return res.data
  } catch (error) {
    console.log(error)
  }
}

const getBoardArticle = async (articleId) => {
  try {
    const res = await http.get(`studyboards/article/${articleId}`)
    return res.data
  } catch (error) {
    console.log(error)
  }
}

const updateViewCountOfBoardArticle = async (articleId) => {
  try {
    await http.put(`studyboards/view-count/${articleId}`)
  } catch (error) {
    console.log(error)
  }
}

// BoardArticleComment
const createArticleComment = async ({ articleId, parentId = 0, content }) => {
  try {
    const res = await http.post(`studycomment/${articleId}/${parentId}`, {
      content,
    })
    return res.data
  } catch (error) {
    console.log(error)
  }
}
const getArticleComment = async (articleId) => {
  try {
    const res = await http.get(`studycomment/${articleId}`)
    const comments = []
    res.data.forEach((c) => {
      if (c.depth === 0) {
        comments.push({ ...c, reCommentList: [] })
      } else {
        comments[comments.length - 1].reCommentList.push(c)
      }
    })
    return comments
  } catch (error) {
    console.log(error)
  }
}
const deleteArticleComment = async (commentId) => {
  try {
    await http.delete(`studycomment/${commentId}`)
  } catch (error) {
    console.log(error)
  }
}

export default {
  createStudy,
  editStudy,
  getStudyList,
  getStudy,
  submitApplication,
  getStudyBoards,
  createBoardArticle,
  getBoardArticles,
  updateBoardArticle,
  getBoardArticle,
  updateViewCountOfStudyArticle,
  updateViewCountOfBoardArticle,
  createArticleComment,
  getArticleComment,
  deleteArticleComment,
}
