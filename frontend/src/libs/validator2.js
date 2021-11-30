export const requiredValidator = (value) => {
  if (typeof value === "string") {
    const type = "required"
    if (value.length === 0) {
      return {
        type,
        status: false,
        message: "필수 입력 요소입니다",
      }
    }
    return {
      type,
      status: true,
    }
  } else {
    alert("requiredValidator에 올바른 타입을 입력하세요")
  }
}

export const emailValidator = (value) => {
  if (typeof value !== "string") {
    alert("emailValidator 올바른 타입을 입력하세요")
    return
  }
  const regex = /([\d\w-_]+)[@]([\w]+)[.]([\w]{1,3})/
  const type = "invalidEmail"
  if (!regex.test(value)) {
    return {
      type,
      status: false,
      message: "이메일을 다시 확인하세요",
    }
  }
  return {
    type,
    status: true,
  }
}

export const passwordValidator = (value) => {
  if (typeof value !== "string") {
    alert("passwordValidator에 올바른 타입을 입력하세요")
    return
  }
  const regex =
    /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,255}$/
  const type = "invalidPassword"
  if (!regex.test(value)) {
    return {
      type,
      status: false,
      message: "대소문자, 숫자, 특수문자를 포함하여 8자리 이상으로 입력하세요",
    }
  }
  return {
    type,
    status: true,
  }
}

export const confirmPasswordValidator = (value, fieldKey, formFields) => {
  const password = formFields.password.value
  const confirmPassword = value
  const type = "invalidConfirmPassword"
  if (password !== confirmPassword) {
    return {
      fieldKey,
      type,
      status: false,
      message: "비밀번호가 일치하지 않습니다",
    }
  }
  return {
    fieldKey,
    type,
    status: true,
  }
}

export const nameValidator = (value) => {
  const regex = /^[가-힣]{1,8}|[a-zA-Z]{2,8}$/
  const type = "invalidName"
  if (!regex.test(value)) {
    return {
      type,
      status: false,
      message: "한글(1 - 8자) 또는 영어(2 - 8자)로 입력하세요.",
    }
  }
  return {
    type,
    status: true,
  }
}
