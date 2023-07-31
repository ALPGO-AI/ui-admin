"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
const block_basekit_server_api_1 = require("@lark-opdev/block-basekit-server-api");
const fetch = (...args) => Promise.resolve().then(() => __importStar(require('node-fetch'))).then(({ default: fetch }) => fetch(...args));
block_basekit_server_api_1.basekit.addAction({
    formItems: [
        {
            itemId: 'text',
            label: '源文本',
            required: true,
            component: block_basekit_server_api_1.Component.Input,
            componentProps: {
                mode: 'textarea',
                placeholder: '请输入源文本或选择引用列',
            }
        },
        {
            itemId: 'transformType',
            label: '转换类型',
            required: true,
            component: block_basekit_server_api_1.Component.SingleSelect,
            componentProps: {
                options: [
                    {
                        label: '生成字艺',
                        value: 'generateFontArt',
                    },
                ]
            }
        }
    ],
    // 定义运行逻辑
    execute: async function (args, context) {
        // 从运行时入参 args 中读取实际的源文本 text 和转换类型 transformType
        const { text = '', transformType } = args;
        // 根据转换类型将源文本做大小写转换
        const generateFontArt = (text) => {
            return `https://api.alpgo.cc/prod-api/sdtool/pattern/fontart/${text}/64`;
        };
        const outputText = transformType === 'generateFontArt'
            ? generateFontArt(text) : text;
        // 返回转换后的数据
        return {
            text: outputText,
        };
    },
    // 定义节点出参
    resultType: {
        // 声明返回为对象
        type: block_basekit_server_api_1.ParamType.Object,
        properties: {
            // 声明 text 属性
            text: {
                // 声明 text 字段类型为 link
                type: block_basekit_server_api_1.ParamType.Link,
                // 声明在节点 UI 上展示的文案为「转换结果」
                label: '转换结果',
            },
        }
    }
});
exports.default = block_basekit_server_api_1.basekit;
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmVnaXN0ZXIuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi9zcmMvcmVnaXN0ZXIudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OztBQUFBLG1GQUFxRjtBQUNyRixNQUFNLEtBQUssR0FBRyxDQUFDLEdBQUcsSUFBSSxFQUFFLEVBQUUsQ0FBQyxrREFBTyxZQUFZLElBQUUsSUFBSSxDQUFDLENBQUMsRUFBQyxPQUFPLEVBQUUsS0FBSyxFQUFDLEVBQUUsRUFBRSxDQUFDLEtBQUssQ0FBQyxHQUFHLElBQUksQ0FBQyxDQUFDLENBQUM7QUFFM0Ysa0NBQU8sQ0FBQyxTQUFTLENBQUM7SUFDaEIsU0FBUyxFQUFFO1FBQ1Q7WUFDRSxNQUFNLEVBQUUsTUFBTTtZQUNkLEtBQUssRUFBRSxLQUFLO1lBQ1osUUFBUSxFQUFFLElBQUk7WUFDZCxTQUFTLEVBQUUsb0NBQVMsQ0FBQyxLQUFLO1lBQzFCLGNBQWMsRUFBRTtnQkFDZCxJQUFJLEVBQUUsVUFBVTtnQkFDaEIsV0FBVyxFQUFFLGNBQWM7YUFDNUI7U0FDRjtRQUNEO1lBQ0UsTUFBTSxFQUFFLGVBQWU7WUFDdkIsS0FBSyxFQUFFLE1BQU07WUFDYixRQUFRLEVBQUUsSUFBSTtZQUNkLFNBQVMsRUFBRSxvQ0FBUyxDQUFDLFlBQVk7WUFDakMsY0FBYyxFQUFFO2dCQUNkLE9BQU8sRUFBRTtvQkFDUDt3QkFDRSxLQUFLLEVBQUUsTUFBTTt3QkFDYixLQUFLLEVBQUUsaUJBQWlCO3FCQUN6QjtpQkFDRjthQUNGO1NBQ0Y7S0FDRjtJQUNELFNBQVM7SUFDVCxPQUFPLEVBQUUsS0FBSyxXQUFVLElBQUksRUFBRSxPQUFPO1FBQ25DLGlEQUFpRDtRQUNqRCxNQUFNLEVBQUUsSUFBSSxHQUFHLEVBQUUsRUFBRSxhQUFhLEVBQUUsR0FBRyxJQUFJLENBQUM7UUFDMUMsbUJBQW1CO1FBQ25CLE1BQU0sZUFBZSxHQUFHLENBQUMsSUFBSSxFQUFFLEVBQUU7WUFDL0IsT0FBTyx3REFBd0QsSUFBSSxLQUFLLENBQUM7UUFDM0UsQ0FBQyxDQUFBO1FBQ0QsTUFBTSxVQUFVLEdBQUcsYUFBYSxLQUFLLGlCQUFpQjtZQUNwRCxDQUFDLENBQUMsZUFBZSxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUMsQ0FBQyxJQUFJLENBQUM7UUFDakMsV0FBVztRQUNYLE9BQU87WUFDTCxJQUFJLEVBQUUsVUFBVTtTQUNqQixDQUFDO0lBQ0osQ0FBQztJQUNELFNBQVM7SUFDVCxVQUFVLEVBQUU7UUFDVixVQUFVO1FBQ1YsSUFBSSxFQUFFLG9DQUFTLENBQUMsTUFBTTtRQUN0QixVQUFVLEVBQUU7WUFDUixhQUFhO1lBQ2IsSUFBSSxFQUFFO2dCQUNKLHFCQUFxQjtnQkFDckIsSUFBSSxFQUFFLG9DQUFTLENBQUMsSUFBSTtnQkFDcEIseUJBQXlCO2dCQUN6QixLQUFLLEVBQUUsTUFBTTthQUNkO1NBQ0o7S0FDRjtDQUNGLENBQUMsQ0FBQztBQUVILGtCQUFlLGtDQUFPLENBQUMifQ==