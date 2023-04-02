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
            <el-popover
                style="position: absolute; bottom: 25px;"
                placement="right"
                width="800"
                trigger="hover">
                <el-table :data="taskList" style="min-height: 500px;" :default-expand-all="true">
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table
                            :show-header="false"
                            :data="props.row.list"
                            style="width: 100%">
                                <el-table-column
                                    type="index"
                                    width="50">
                                </el-table-column>
                                <el-table-column
                                    prop="envName"
                                    width="250"
                                    label="环境">
                                </el-table-column>
                                <el-table-column
                                    prop="taskName"
                                    width="500"
                                    label="任务">
                                </el-table-column>
                            </el-table>
                        </template>
                        </el-table-column>
                    <el-table-column width="250" property="env" label="环境"></el-table-column>
                    <el-table-column width="500" property="count" label="任务"></el-table-column>
                </el-table>
                <el-button
                    :style="{
                        border: 'none',
                        background: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
                        color: settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor
                    }"
                    slot="reference">
                    <i class="el-icon-notebook-2"></i>
                </el-button>
            </el-popover>
        </el-scrollbar>
    </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
    components: { SidebarItem, Logo },
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
