import http from "@/api/http"

const getMypage = async (email = "") => {
  const res = await http.get(`member/mypage/${email}`)
  return res.data
}

const getMe = async () => {
  const res = await http.get(`member/me`)
  return res.data
}

const getBasicInfo = async () => {
  const res = await http.get(`member/basicinfo`)
  return res.data
}

const updateBasicInfo = async (data) => {
  await http.put(`member/basicinfo`, data)
}

const getSkills = async () => {
  const res = await http.get(`member/skills`)
  const dpositionList = res.data.dpositionList.map((dp) => dp.name)
  const techList = res.data.techList.reduce((acc, dp) => {
    return {
      ...acc,
      [dp.name]: dp.level,
    }
  }, {})
  return {
    position: res.data.position,
    dpositionList,
    techList,
  }
}

const updateSkills = async (data) => {
  await http.put(`member/skills`, data)
}

const getCareerAll = async () => {
  const res = await http.get(`member/careerall`)
  return res.data
}

const createCareer = async (data) => {
  const res = await http.post(`member/career`, data)
  return res.data
}

const getCareer = async (careerId) => {
  const res = await http.get(`member/career/${careerId}`)
  return res.data
}

const updateCareer = async ({ submitData, careerId }) => {
  const res = await http.put(`member/career/${careerId}`, submitData)
  return res.data
}

const deleteCareer = async (careerId) => {
  await http.delete(`member/career/${careerId}`)
}

const createCertification = async (data) => {
  const res = await http.post(`member/certification`, data)
  return res.data
}

const getCertification = async (certificationId) => {
  const res = await http.get(`member/certification/${certificationId}`)
  return res.data
}

const updateCertification = async ({ submitData, certificationId }) => {
  const res = await http.put(
    `member/certification/${certificationId}`,
    submitData
  )
  return res.data
}

const deleteCertification = async (certificationId) => {
  await http.delete(`member/certification/${certificationId}`)
}

const createEdu = async (data) => {
  const res = await http.post(`member/education`, data)
  return res.data
}

const getEdu = async (eduId) => {
  const res = await http.get(`member/education/${eduId}`)
  return res.data
}

const updateEdu = async ({ submitData, eduId }) => {
  const res = await http.put(`member/education/${eduId}`, submitData)
  return res.data
}

const deleteEdu = async (eduId) => {
  await http.delete(`member/education/${eduId}`)
}

const getSNSPortfolio = async () => {
  const res = await http.get(`member/snsportfolio`)
  return res.data
}

const updatePortfolio = async (data) => {
  const res = await http.put(`member/portfolio`, data)
  return res.data
}

const updateSNS = async (data) => {
  const res = await http.put(`member/sns`, data)
  return res.data
}

export default {
  getMypage,
  getMe,
  getBasicInfo,
  updateBasicInfo,
  getSkills,
  updateSkills,
  getCareerAll,
  createCareer,
  getCareer,
  updateCareer,
  deleteCareer,
  createCertification,
  getCertification,
  updateCertification,
  deleteCertification,
  createEdu,
  getEdu,
  updateEdu,
  deleteEdu,
  getSNSPortfolio,
  updatePortfolio,
  updateSNS,
}
