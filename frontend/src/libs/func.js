import moment from "moment"
import "moment/locale/ko"

import {
  emailValidator,
  nameValidator,
  passwordValidator,
  requiredValidator,
} from "./validator2"

export class InputFormFieldMaker {
  constructor(
    key,
    value = "",
    disabled = false,
    label = "라벨",
    type = "string",
    placeholder = ""
  ) {
    this.key = key
    this.value = value
    this.errors = {}
    this.disabled = disabled

    switch (key) {
      case "email": {
        this.label = "이메일"
        this.type = "string"
        this.placeholder = "이메일을 입력하세요"
        this.validators = [emailValidator]
        break
      }
      case "password": {
        this.label = "비밀번호"
        this.type = "password"
        this.placeholder = "대소문자, 숫자, 특수문자 포함 8자 이상"
        this.validators = [passwordValidator]
        break
      }
      case "confirmPassword": {
        this.label = "비밀번호 확인"
        this.type = "password"
        this.placeholder = "동일한 비밀번호를 입력하세요"
        this.validators = []
        break
      }
      case "authCode": {
        this.label = "인증코드"
        this.type = "string"
        this.placeholder = "인증코드를 입력하세요"
        this.validators = [requiredValidator]
        break
      }
      case "nickname": {
        this.label = "닉네임"
        this.type = "string"
        this.placeholder = "닉네임을 입력하세요"
        this.validators = [requiredValidator]
        break
      }
      case "name": {
        this.label = "이름"
        this.type = "string"
        this.placeholder = "이름을 입력하세요"
        this.validators = [nameValidator]
        break
      }
      case "title": {
        this.label = "제목"
        this.type = "string"
        this.placeholder = "ex) DTO 사용방식에 대한 고찰"
        this.validators = [requiredValidator]
        break
      }
      case "tags": {
        this.label = "태그"
        this.type = "string"
        this.placeholder = "ex) Todo, FE, BE, DB, 일정, 회의록"
        this.validators = []
        break
      }
      case "projectName": {
        this.label = "프로젝트 이름"
        this.type = "string"
        this.placeholder = "11월까지 진행하는 사이드 프로젝트"
        this.validators = [requiredValidator]
        break
      }
      case "schedule": {
        this.label = "일정"
        this.type = "string"
        this.placeholder = "ex) 주말 10시 - 18시 / 평일 논의"
        this.validators = []
        break
      }
      default: {
        this.label = label
        this.type = type
        this.placeholder = placeholder
        this.validators = []
      }
    }
  }

  updateErrors(res) {
    const { type, status } = res
    if (status) {
      delete this.errors[type]
    } else {
      this.errors[type] = res.message
    }
  }

  validate() {
    return this.validators.every((v) => {
      const res = v(this.value)
      this.updateErrors(res)
      return res.status
    })
  }
}

export class EmailFormFieldMaker {
  constructor() {
    this.key = "email"
    this.label = "이메일"
    this.type = "string"
    this.value = ""
    this.disabled = false
    this.placeholder = "이메일을 입력하세요"
    this.errors = {}
    this.validators = [emailValidator]
    this.buttonDisabled = true
    this.buttonLabel = "메일 인증"
  }
  validate() {
    return this.validators.every((validator) => {
      const validateRes = validator(this.value, this.key)
      this.updateErrors(validateRes)
      return validateRes.status
    })
  }
  updateInputDisabled(status) {
    this.disabled = status
  }
  updateButtonLabel(label) {
    this.buttonLabel = label
  }
  updateButtonDisabled(status) {
    this.buttonDisabled = status
  }
  updateErrors(validateRes) {
    const { status, type } = validateRes
    if (status) {
      delete this.errors[type]
    } else {
      this.errors[type] = validateRes.message
    }
  }
}

export class AuthCodeFormFieldMaker {
  constructor() {
    this.key = "authCode"
    this.label = "인증번호 입력"
    this.type = "string"
    this.value = ""
    this.placeholder = "메일로 전송된 인증번호를 입력하세요"
    this.errors = {}
    this.validators = [requiredValidator]
    this.buttonDisabled = true
    this.buttonLabel = "확인"
  }
  validate() {
    return this.validators.every((validator) => {
      const validateRes = validator(this.value, this.key)
      this.updateErrors(validateRes)
      return validateRes.status
    })
  }
  updateButtonLabel(label) {
    this.buttonLabel = label
  }
  updateButtonDisabled(status) {
    this.buttonDisabled = status
  }
  updateErrors(validateRes) {
    const { status, type } = validateRes
    if (status) {
      delete this.errors[type]
    } else {
      this.errors[type] = validateRes.message
    }
  }
}

// 2021-10-31T12:54:37.908847
// 2021-10-31T12:57:42.164204

export const dateFormatter = (date) => {
  console.log(date)
  switch (moment(date).diff(moment(Date.now()), "days")) {
    case 0: {
      return moment(date).fromNow()
    }
    default: {
      return moment(date).format("YYYY-MM-DD")
    }
  }
}
