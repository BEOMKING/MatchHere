<template>
  <Modal @closeModal="handleCloseModal">
    <h4 class="mb-4">자격증 추가</h4>
    <div class="grid gap-4 mb-10">
      <InputFormField :field="nameField" v-model="nameField.value" />
      <InputFormField :field="gradeField" v-model="gradeField.value" />
      <InputFormField
        :field="organizationField"
        v-model="organizationField.value"
      />
      <InputFormField :field="codeField" v-model="codeField.value" />
      <div class="field grid gap-2">
        <label class="text-sm font-medium">{{ issuedDate.label }}</label>
        <input
          class="py-2 px-4 border border-gray-400 rounded"
          type="date"
          name="issuedDate"
          :disabled="issuedDate.disabled"
          v-model="issuedDate.value"
        />
      </div>
      <div class="field grid gap-2">
        <label class="text-sm font-medium">{{ expiredDate.label }}</label>
        <input
          class="py-2 px-4 border border-gray-400 rounded"
          type="date"
          name="expiredDate"
          :disabled="expiredDate.disabled && !isExpire"
          v-model="expiredDate.value"
        />
        <div class="flex gap-2 items-center">
          <input
            type="checkbox"
            name="is_expire"
            id="is_expire"
            v-model="isExpire"
          />
          <label for="is_expire">유효기간 있음</label>
        </div>
      </div>
    </div>
    <div class="buttons flex gap-4 w-full justify-center">
      <button
        class="rounded py-2 px-6 text-sm font-bold bg-blue-500 text-white"
        @click="handleSubmit"
      >
        확인
      </button>
      <button
        class="
          rounded
          py-2
          px-6
          text-sm
          font-bold
          text-gray-600
          border border-gray-400
        "
        @click="handleCloseModal"
      >
        취소
      </button>
    </div>
  </Modal>
</template>

<script>
import Modal from "@/components/common/Modal.vue"
import InputFormField from "@/components/common/formField/InputFormField.vue"
import { reactive, ref } from "@vue/reactivity"
import { InputFormFieldMaker } from "@/libs/func"
import { useStore } from "vuex"
import { onMounted } from "@vue/runtime-core"

export default {
  name: "CertificationFormModal",
  components: { Modal, InputFormField },
  emits: ["closeModal"],
  props: {
    certificationId: Number,
    type: String,
  },
  setup(props, { emit }) {
    const store = useStore()

    const nameField = reactive(
      new InputFormFieldMaker(
        "certificationName",
        "",
        false,
        "자격증 이름",
        "string",
        "ex) 정보처리기사"
      )
    )

    const gradeField = reactive(
      new InputFormFieldMaker(
        "grade",
        "",
        false,
        "등급 (선택)",
        "string",
        "ex) Level7"
      )
    )

    const organizationField = reactive(
      new InputFormFieldMaker(
        "organization",
        "",
        false,
        "발급기관",
        "string",
        "ex) 한국산업인력공단"
      )
    )

    const codeField = reactive(
      new InputFormFieldMaker(
        "code",
        "",
        false,
        "코드 (선택)",
        "string",
        "ex) A-123456"
      )
    )

    const issuedDate = reactive({
      value: "",
      label: "취득일",
      disabled: false,
    })

    const expiredDate = reactive({
      value: "",
      label: "만료일",
      disabled: true,
    })

    const isExpire = ref(false)

    const checkAllValueFilled = () => {
      if (organizationField.value && nameField.value && issuedDate.value) {
        if (expiredDate.value || !isExpire.value) {
          return true
        }
      }
      store.commit("ADD_MESSAGE", {
        text: "내용을 모두 채워주세요",
        type: "warning",
      })
      return false
    }

    const checkStartexpiredDate = () => {
      if (isExpire.value && issuedDate.value > expiredDate.value) {
        store.commit("ADD_MESSAGE", {
          text: "취득일이 만료일보다 늦을 수는 없어요",
          type: "warning",
        })
        return false
      }
      return true
    }

    const handleCloseModal = (payload = {}) => {
      emit("closeModal", { ...payload, type: "certification" })
    }

    onMounted(async () => {
      switch (props.type) {
        case "EDIT": {
          const certificationData = await store.dispatch(
            "member/getCertification",
            props.certificationId
          )
          nameField.value = certificationData.name
          gradeField.value = certificationData.grade
          organizationField.value = certificationData.organization
          codeField.value = certificationData.code
          issuedDate.value = certificationData.issued_date
          expiredDate.value = certificationData.expired_date || ""
          isExpire.value = certificationData.is_expire
        }
      }
    })

    const handleSubmit = async () => {
      if (checkAllValueFilled() && checkStartexpiredDate()) {
        const submitData = {
          name: nameField.value,
          grade: gradeField.value,
          organization: organizationField.value,
          code: codeField.value,
          issued_date: issuedDate.value,
          expired_date: expiredDate.value,
          is_expire: isExpire.value,
        }
        switch (props.type) {
          case "EDIT": {
            const certification = await store.dispatch(
              "member/updateCertification",
              {
                submitData,
                certificationId: props.certificationId,
              }
            )
            if (certification) {
              handleCloseModal({ data: certification, action: "update" })
            }
            console.log(certification)
            break
          }
          case "CREATE": {
            const certification = await store.dispatch(
              "member/createCertification",
              submitData
            )
            if (certification) {
              handleCloseModal({ data: certification, action: "create" })
            }
            break
          }
        }
      }
    }

    return {
      nameField,
      gradeField,
      organizationField,
      codeField,
      issuedDate,
      expiredDate,
      isExpire,
      handleSubmit,
      handleCloseModal,
    }
  },
}
</script>

<style lang="scss" scoped></style>
