<template>
  <Modal @closeModal="handleCloseModal">
    <h4 class="mb-4">포트폴리오</h4>
    <div class="grid gap-4 mb-10">
      <div class="grid gap-2 relative">
        <span class="font-medium text-sm">파일</span>
        <div
          class="
            border
            rounded
            border-gray-400
            relative
            py-2
            px-4
            select-none
            cursor-pointer
          "
          @click="handleClick"
        >
          {{ portfolioFile.file_name || "파일을 업로드하세요" }}
          <span class="material-icons-outlined absolute top-2 right-3">
            file_download
          </span>
        </div>
        <input
          ref="fileInput"
          class="absolute hidden"
          type="file"
          accept="*"
          name="portfolio"
          id="portfolio"
          @change="handleChangeFile"
        />
      </div>
      <InputFormField :field="portfolioUri" v-model="portfolioUri.value" />
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

export default {
  name: "PortfolioFormModal",
  components: { Modal, InputFormField },
  emits: ["closeModal", "updatePortfolio"],
  props: {
    file: [Object, null],
    uri: String,
  },
  setup(props, { emit }) {
    const store = useStore()
    const fileInput = ref(null)
    const portfolioFile = reactive({ ...props.file })
    const portfolioUri = reactive(
      new InputFormFieldMaker(
        "portfolio_uri",
        props.uri,
        false,
        "링크",
        "string",
        "https:// 를 포함하여 입력하세요"
      )
    )

    const handleClick = () => {
      console.log(fileInput.value)
      fileInput.value.click()
    }

    const handleChangeFile = async (e) => {
      const files = e.target.files || e.dataTransfer.files
      if (!files.length) {
        portfolioFile.id = ""
      }
      const formData = new FormData()
      formData.append("file", files[0])
      const { fileName, id } = await store.dispatch("file/uploadFile", formData)
      if (id) {
        portfolioFile.id = id
        portfolioFile.file_name = fileName
      }
    }

    const handleCloseModal = () => {
      emit("closeModal")
    }

    const handleSubmit = async () => {
      const data = {
        portfolio_uri: portfolioUri.value,
        portfolio_uuid: portfolioFile.id,
      }

      const newPortfolio = await store.dispatch("member/updatePortfolio", data)
      if (newPortfolio) {
        emit("updatePortfolio", newPortfolio)
        handleCloseModal()
      }
    }

    return {
      fileInput,
      portfolioFile,
      portfolioUri,
      handleClick,
      handleCloseModal,
      handleChangeFile,
      handleSubmit,
    }
  },
}
</script>

<style></style>
