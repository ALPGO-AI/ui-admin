<template>
    <div :class="{'has-logo':showLogo}" :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
        <logo v-if="showLogo" :collapse="isCollapse" />
        <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
            <el-menu
                :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="false"
                mode="vertical"
            >
                <sidebar-item
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path"
                />
            </el-menu>
            <el-button
                @click="showTaskBoard"
                :style="{
                    border: 'none',
                    background: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
                    color: settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor
                }"
                slot="reference">
                <i class="el-icon-notebook-2"></i>
            </el-button>
        </el-scrollbar>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";
import { formatDate } from "@/utils";

export default {
    components: { SidebarItem, Logo },
    methods: {
        removeEnvTasks(env) {
            this.$store.dispatch("task/removeEnvTasks", env);
        },
        formatterTime (value) {
            return formatDate(value.createTime)
        },
        showTaskBoard () {
            this.$store.dispatch("task/showTaskBoard");
        }
    },
    computed: {
        ...mapState(["settings"]),
        ...mapGetters(["sidebarRouters", "sidebar", "taskList"]),
        activeMenu() {
            const route = this.$route;
            const { meta, path } = route;
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
                return meta.activeMenu;
            }
            return path;
        },
        showLogo() {
            return this.$store.state.settings.sidebarLogo;
        },
        variables() {
            return variables;
        },
        isCollapse() {
            return !this.sidebar.opened;
        }
    }
};
</script>
