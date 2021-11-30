<template>
  <Modal @closeModal="handleCloseModal">
    <h4 class="mb-4">SNS</h4>
    <div class="grid gap-4 mb-10">
      <InputFormField
        v-for="snsField in snsFormFields"
        :key="snsField.key"
        :field="snsField"
        v-model="snsField.value"
      />
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
import { reactive } from "@vue/reactivity"
import { InputFormFieldMaker } from "@/libs/func"
import { useStore } from "vuex"

export default {
  name: "SNSFormModal",
  components: { Modal, InputFormField },
  emits: ["closeModal", "updateSNS"],
  props: {
    snsList: {
      type: Object,
    },
  },
  setup(props, { emit }) {
    const store = useStore()
    const snsFormFields = reactive({
      github: new InputFormFieldMaker(
        "github",
        "",
        false,
        "Github",
        "url",
        "https://"
      ),
      facebook: new InputFormFieldMaker(
        "facebook",
        "",
        false,
        "Facebook",
        "url",
        "https://"
      ),
      twitter: new InputFormFieldMaker(
        "twitter",
        "",
        false,
        "Twitter",
        "url",
        "https://"
      ),
      baekjoon: new InputFormFieldMaker(
        "baekjoon",
        "",
        false,
        "백준",
        "url",
        "https://"
      ),
      optional: new InputFormFieldMaker(
        "optional",
        "",
        false,
        "기타",
        "url",
        "https://"
      ),
    })

    const handleCloseModal = () => {
      emit("closeModal")
    }

    const handleSubmit = async () => {
      const data = {}

      Object.keys(snsFormFields).forEach((key) => {
        if (snsFormFields[key].value) {
          data[key] = snsFormFields[key].value
        }
      })

      const newSNSList = await store.dispatch("member/updateSNS", {
        snsList: data,
      })
      if (newSNSList) {
        emit("updateSNS", newSNSList)
        handleCloseModal()
      }
    }

    props.snsList.forEach((sns) => {
      console.log(snsFormFields[sns.snsName])
      snsFormFields[sns.snsName].value = sns.snsAccount
    })

    return {
      snsFormFields,
      handleCloseModal,
      handleSubmit,
    }
  },
}
</script>

<style></style>
