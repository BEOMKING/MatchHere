<template>
  <router-link
    :to="{
      name: 'StudyBoardArticleDetail',
      params: { articleId: article.articleId },
    }"
    class="article-link"
  >
    <div class="article-info">
      <p class="title">{{ article.title }}</p>
      <div class="tags flex gap-2">
        <Tag v-for="tag in article.tags" :key="tag" :text="tag" type="board" />
      </div>
      <div class="created flex gap-2">
        <Member :user="article.writer" />
      </div>
    </div>
    <div class="icon-wrapper">
      <router-link
        :to="{
          name: 'StudyBoardArticleEdit',
          params: { articleId: article.articleId },
        }"
      >
        수정
      </router-link>
    </div>
  </router-link>
</template>

<script>
import Tag from "@/components/common/Tag.vue"
import Member from "@/components/common/Member.vue"
export default {
  components: { Tag, Member },
  name: "BoardArticleLink",
  props: {
    article: Object,
  },
}
</script>

<style lang="scss" scoped>
.article-link {
  @apply flex p-4 rounded-md bg-white shadow-sm;

  .article-info {
    @apply flex-1 grid gap-2;
  }
}
</style>
