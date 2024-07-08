<template>
  <div class="user-wrapper">
    <div class="content-box display-flex align-items" :style="{ 'margin-right': isMobile() ? '30px' : '' }">
      <div class="display-flex align-items">
        <a-button :icon="v2 ? 'vertical-left' : 'vertical-right'" ghoat @click="toNewTheme">{{ v2 ? '返回旧版' : '前往新版' }}</a-button>
        <a-tooltip placement="bottomRight">
          <template slot="title">
            <span>前往登录, 体验更多功能</span>
          </template>
          <a-avatar v-if="userInfo" @click="toLogo" icon="user" style="cursor: pointer"/>
        </a-tooltip>
      </div>
      <div class="position-relative" v-if="token && roles !== 'ADMIN'">
        <a-dropdown :trigger="['click']">
          <span class="display-flex align-items pointer" style="gap: 10px">
            <a-avatar class="avatar" size="small" :src="avatar"/>
            <span>{{ nickname }}</span>
          </span>
          <a-menu slot="overlay">
            <a-menu-item @click="handleLogout">
              <span>退出登录</span>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </div>
      <div class="display-flex align-items" v-if="token && roles === 'ADMIN'">
        <a-popover trigger="click" placement="bottomRight" :overlayStyle="{ width:'250px' }" v-model="visible">
          <template slot="content">
            <div style="color: #999; gap: 5px;align-items: end" class="display-flex">
              <div class="position-relative">
                <a-avatar class="avatar" size="default" shape="square" :src="avatar"/>
              </div>
              <span class="font-size-14">ID: </span><span class="font-size-14">{{ uId }}</span>
              <img src="https://edu-2330.oss-cn-beijing.aliyuncs.com/icon-images/member.svg" alt="member" style="width: 20px;margin-bottom: 3px">
            </div>
            <a-divider style="margin: 15px 0!important;"/>
            <div class="padding-l-10 margin-b-14 pointer active-center" style="border-radius: 15px" v-if="roles === 'ADMIN'">
              <div class="display-flex align-items" style="gap: 6px" @click="toCenter">
                <a-icon type="user" />
                <span>后台运营</span>
              </div>
            </div>
            <div class="padding-l-10 pointer logout-active" style="width: 80%;border-radius: 15px;color: #f67575;">
              <div class="display-flex align-items" style="gap: 6px" @click="handleLogout">
                <a-icon type="logout" />
                <span>退出登录</span>
              </div>
            </div>
          </template>
          <span class="action ant-dropdown-link user-dropdown-menu">
            <a-avatar class="avatar" size="small" :src="avatar"/>
            <span>{{ nickname }}</span>
          </span>
        </a-popover>
      </div>
    </div>
  </div>
</template>

<script>
import NoticeIcon from '@/components/NoticeIcon'
import { mapActions, mapGetters, mapState } from 'vuex'
import { DEVICE_TYPE } from '@/utils/device'
export default {
  name: 'UserMenu',
  components: {
    NoticeIcon
  },
  data () {
    return {
      visible: false // 弹窗是否显示
    }
  },
  computed: {
    ...mapGetters(['nickname', 'avatar', 'userInfo', 'token', 'v2']),
    ...mapState({
      device: state => state.app.device
    }),
    userInfo () {
      return JSON.stringify(this.$store.state.user.info) === '{}'
    },
    userData () {
      return this.$store.getters.userInfo
    },
    userId () {
      return this.userData.userInfo.userId
    },
    roles () {
      return this.$store.state.user && this.$store.state.user.roles[0]
    },
    maxThesisCount () {
      return this.userData && this.userData.userInfo.maxThesisCount
    },
    isMember () {
      if (this.token) {
        return this.userData && this.userData.userInfo.member
      }
      return false
    },
    uId () {
      return this.userData.userInfo && this.userData.userInfo.userId.slice(0, 8).toUpperCase()
    }
  },
  methods: {
    ...mapActions(['Logout']),
    toNewTheme () {
      if (this.v2) {
        this.$router.push({
          name: 'Workplace'
        })
      } else {
        this.$router.push({
          name: 'NewTheme'
        })
      }
      this.$store.commit('TOGGLE_THEME_V2', !this.v2)
    },
    handleLogout () {
      this.visible = false
      this.$confirm({
        title: '提示',
        content: '真的要注销登录吗 ?',
        onOk: () => {
          return this.Logout({}).then(() => {
            setTimeout(() => {
              window.location.reload()
            }, 16)
          }).catch(err => {
            this.$message.error({
              title: '错误',
              description: err.message
            })
          })
        },
        onCancel () {
        }
      })
    },
    toCenter () {
      this.visible = false
      this.$router.push({
        name: 'center'
      })
    },
    toLogo () {
      this.$router.push({
        name: 'login'
      })
    },
    isMobile () {
      return this.device === DEVICE_TYPE.MOBILE
    }
  }
}
</script>
<style lang='less' scoped>
.active-center:hover {
  color: #4819a8;
}
.ant-card {
  ::v-deep .ant-card-head {
    border-bottom: unset!important;
  }
  ::v-deep .ant-card-body {
    padding: 0 24px 10px!important;
  }
}
::v-deep .ant-badge-status-text {
  font-size: 12px;
}
.active-type {
  border-color: #722ed1!important
}
.tag-info-member {
  display: inline-block;
  position: absolute;
  top: -10px;
  right: -10px;
  background: rgb(245, 34, 45);
  color: rgb(255, 255, 255);
  font-size: 12px;
  border-radius: 8px;
  padding: 2px 4px;
}
.description-invert {
  height: 100%;
  background: rgb(230, 247, 255);
  border-radius: 10px;
  padding-bottom: 10px;
}
.title-font {
  font-family: hanyiyuanrongti;
}
</style>
