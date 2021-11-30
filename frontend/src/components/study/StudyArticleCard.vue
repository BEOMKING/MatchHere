<template>
  <router-link
    class="study-card"
    :class="{ small }"
    :to="{ name: 'StudyArticle', params: { studyId: study.id } }"
    @mousedown="handleMousedown"
    @mouseup="handleMouseup"
  >
    <div class="thumbnail-wrapper">
      <img class="" :src="study.coverPicUri" alt="" draggable="false" />
      <p class="recruit-state">{{ study.recruitmentState }}</p>
    </div>
    <div class="info-wrapper">
      <p class="name">{{ study.name }}</p>
      <div class="tags mb-2">
        <p class="tag subject" v-for="topic in study.topics" :key="topic.name">
          {{ topic.name }}
        </p>
      </div>
      <div class="tags mb-2">
        <p class="tag study-state">{{ study.studyProgressState }}</p>
      </div>
      <p class="city">지역: {{ study.city }}</p>
    </div>
    <div class="bottom-wrapper">
      <div class="left">
        <div class="profile-img-wrapper"></div>
        <span class="host-name">{{ study.host.name }}</span>
        <span class="created">{{ formattedDate }}</span>
      </div>
      <button>
        <span class="material-icons">more_vert</span>
      </button>
    </div>
  </router-link>
</template>

<script>
import { computed, ref } from "vue"
import { dateFormatter } from "@/libs/func"

export default {
  name: "StudyArticleCard",
  props: {
    study: Object,
    small: {
      type: Boolean,
      default: false,
    },
  },
  setup(props) {
    const mousePosition = ref([0, 0])
    const formattedDate = computed(() => dateFormatter(props.study.createDate))

    const handleMousedown = (e) => {
      console.log("down")
      mousePosition.value = [e.clientX, e.clientY]
    }
    const handleMouseup = (e) => {
      if (
        mousePosition.value[0] !== e.clientX ||
        mousePosition.value[1] !== e.clientY
      ) {
        return
      }
      console.log("click")
    }
    return { formattedDate, handleMousedown, handleMouseup }
  },
}
</script>

<style lang="scss" scoped>
.study-card {
  @apply inline-block rounded-md overflow-hidden shadow-md w-full flex-shrink-0 select-none bg-white;

  &:hover {
    @apply bg-blue-50;
  }

  &.small {
    /* max-width: 320px; */
  }

  .thumbnail-wrapper {
    padding-top: 50%;
    @apply relative;

    img {
      @apply absolute inset-0 w-full h-full object-cover;
    }

    .recruit-state {
      @apply absolute top-2 right-2 py-1 px-2 text-sm font-bold rounded bg-green-100 text-green-600 shadow select-none;
    }
  }

  .info-wrapper {
    @apply p-4 border-b border-gray-200;

    .name {
      @apply font-medium mb-2;
    }

    .tags {
      @apply flex flex-wrap gap-2;

      .tag {
        @apply py-1 px-2 text-sm font-bold rounded;

        &.subject {
          @apply bg-pink-100 text-pink-600;
        }
        &.study-state {
          @apply bg-blue-100 text-blue-600;
        }
        &.recruit-state {
          @apply bg-green-100 text-green-600;
        }
      }
    }

    .city {
      @apply text-sm text-gray-600;
    }
  }

  .bottom-wrapper {
    @apply p-2 flex items-center justify-between;
    .left {
      @apply flex gap-2 items-center;

      .profile-img-wrapper {
        @apply rounded-full bg-teal-200 w-6 h-6;
      }
      .host-name {
        @apply text-sm font-medium;
      }
      .created {
        @apply text-sm text-gray-700;
      }
    }

    button {
      @apply flex;
    }
  }
}
</style>
