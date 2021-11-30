import http from "@/api/http.js"
// import store from "@/store"

const getMyClubList = async () => {
  const res = await http.get(`/project/myclublist`)
  return res.data
}

const createProject = async (formData) => {
  const res = await http.post(`/project`, formData)
  return res.data
}

const updateProject = async (formData, projectId) => {
  console.log(formData)
  console.log(projectId)
  const res = await http.put(`/project/${projectId}`, formData)
  return res.data
}

const getProject = async (projectId) => {
  const res = await http.get(`/project/${projectId}`)
  return res.data
}

const getInfoForUpdate = async (projectId) => {
  const res = await http.get(`/project/infoforupdate/${projectId}`)
  return res.data
}

const projectApply = async (reqForm, projectId) => {
  const res = await http.post(`/projectapplication/${projectId}`, reqForm)
  return res.data
}

const getAllApplication = async (projectId) => {
  const res = await http.get(`/projectapplication/all/${projectId}`)
  return res.data
}

const getApplication = async (projectId, memberId) => {
  const res = await http.get(`/projectapplication/one/${projectId}/${memberId}`)
  return res.data
}

const getProjectMemberList = async (projectId) => {
  const res = await http.get(`/project/member/${projectId}`)
  return res.data
}

const acceptApplication = async (projectId, memberId) => {
  const res = await http.post(
    `/projectapplication/approval/${projectId}/${memberId}`
  )
  return res
}

const refuseApplication = async (projectId, memberId) => {
  const res = await http.delete(`/projectapplication/${projectId}/${memberId}`)
  return res.data
}

const getBoardList = async (projectId) => {
  const res = await http.get(`/project/${projectId}/boards`)
  return res.data
}

const getBoardArticleList = async (boardId) => {
  const res = await http.get(`/projectboards/${boardId}/articles`)
  return res.data
}

const createBoardArticle = async (reqForm) => {
  const res = await http.post(`/projectboards`, reqForm)
  return res.data
}

const updateBoardArticle = async (reqForm, articleId) => {
  const res = await http.put(`/projectboards/${articleId}`, reqForm)
  return res.data
}

const deleteBoardArticle = async (articleId) => {
  console.log(articleId)
  const res = await http.delete(`/projectboards/${articleId}`)
  return res.data
}

const getBoardArticleDetail = async (articleId) => {
  const res = await http.get(`/projectboards/article/${articleId}`)
  return res.data
}

const getArticleComment = async (articleId) => {
  const res = await http.get(`/projectcomment/${articleId}`)
  return res.data
}

const createComment = async (content, articleId, parentId) => {
  const res = await http.post(`/projectcomment/${articleId}/${parentId}`, {
    content,
  })
  return res.data
}

const getAuthority = async (projectId) => {
  const res = await http.get(`project/authority/${projectId}`)
  return res.data
}

const changeAuthority = async (projectId, memberId, authority) => {
  const res = await http.put(
    `/project/authority/${projectId}/${memberId}/${authority}`
  )
  return res.data
}

const changeRole = async (projectId, memberId, role) => {
  const res = await http.put(`/project/role/${projectId}/${memberId}/${role}`)
  return res.data
}

const updatePicture = async (projectId, uuid) => {
  const res = await http.put(`/project/cover-pic/${projectId}/${uuid}`)
  return res.data
}

const viewCount = async (projectId) => {
  const res = await http.put(`/project/view-count/${projectId}`)
  return res.data
}
export default {
  getMyClubList,
  createProject,
  updateProject,
  getProject,
  getInfoForUpdate,
  projectApply,
  getAllApplication,
  getApplication,
  getProjectMemberList,
  refuseApplication,
  acceptApplication,
  getBoardList,
  getBoardArticleList,
  createBoardArticle,
  updateBoardArticle,
  deleteBoardArticle,
  getBoardArticleDetail,
  getArticleComment,
  createComment,
  getAuthority,
  changeAuthority,
  changeRole,
  updatePicture,
  viewCount,
}
