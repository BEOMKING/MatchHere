import http from "@/api/http.js"

const uploadFile = async (file) => {
  const res = await http.post(`/file/uploadFile`, file, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  })
  return res.data
}
const uploadFile2 = async (file) => {
  const res = await http.post(`/file/uploadFile`, file, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  })
  return res.data
}

export default {
  uploadFile,
  uploadFile2,
}
