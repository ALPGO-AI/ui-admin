"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const block_basekit_server_api_1 = require("@lark-opdev/block-basekit-server-api");
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
                        label: '转为大写',
                        value: 'toUpperCase',
                    },
                    {
                        label: '转为小写',
                        value: 'toLowerCase',
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
        const outputText = transformType === 'toUpperCase'
            ? text.toUpperCase()
            : text.toLowerCase();
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
                // 声明 text 字段类型为 string
                type: block_basekit_server_api_1.ParamType.String,
                // 声明在节点 UI 上展示的文案为「转换结果」
                label: '转换结果',
            },
        }
    }
});
exports.default = block_basekit_server_api_1.basekit;
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicmVnaXN0ZXIuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi9zcmMvcmVnaXN0ZXIudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7QUFBQSxtRkFBcUY7QUFFckYsa0NBQU8sQ0FBQyxTQUFTLENBQUM7SUFDaEIsU0FBUyxFQUFFO1FBQ1Q7WUFDRSxNQUFNLEVBQUUsTUFBTTtZQUNkLEtBQUssRUFBRSxLQUFLO1lBQ1osUUFBUSxFQUFFLElBQUk7WUFDZCxTQUFTLEVBQUUsb0NBQVMsQ0FBQyxLQUFLO1lBQzFCLGNBQWMsRUFBRTtnQkFDZCxJQUFJLEVBQUUsVUFBVTtnQkFDaEIsV0FBVyxFQUFFLGNBQWM7YUFDNUI7U0FDRjtRQUNEO1lBQ0UsTUFBTSxFQUFFLGVBQWU7WUFDdkIsS0FBSyxFQUFFLE1BQU07WUFDYixRQUFRLEVBQUUsSUFBSTtZQUNkLFNBQVMsRUFBRSxvQ0FBUyxDQUFDLFlBQVk7WUFDakMsY0FBYyxFQUFFO2dCQUNkLE9BQU8sRUFBRTtvQkFDUDt3QkFDRSxLQUFLLEVBQUUsTUFBTTt3QkFDYixLQUFLLEVBQUUsYUFBYTtxQkFDckI7b0JBQ0Q7d0JBQ0UsS0FBSyxFQUFFLE1BQU07d0JBQ2IsS0FBSyxFQUFFLGFBQWE7cUJBQ3JCO2lCQUNGO2FBQ0Y7U0FDRjtLQUNGO0lBQ0QsU0FBUztJQUNULE9BQU8sRUFBRSxLQUFLLFdBQVUsSUFBSSxFQUFFLE9BQU87UUFDbkMsaURBQWlEO1FBQ2pELE1BQU0sRUFBRSxJQUFJLEdBQUcsRUFBRSxFQUFFLGFBQWEsRUFBRSxHQUFHLElBQUksQ0FBQztRQUMxQyxtQkFBbUI7UUFDbkIsTUFBTSxVQUFVLEdBQUcsYUFBYSxLQUFLLGFBQWE7WUFDaEQsQ0FBQyxDQUFDLElBQUksQ0FBQyxXQUFXLEVBQUU7WUFDcEIsQ0FBQyxDQUFDLElBQUksQ0FBQyxXQUFXLEVBQUUsQ0FBQztRQUN2QixXQUFXO1FBQ1gsT0FBTztZQUNMLElBQUksRUFBRSxVQUFVO1NBQ2pCLENBQUM7SUFDSixDQUFDO0lBQ0QsU0FBUztJQUNULFVBQVUsRUFBRTtRQUNWLFVBQVU7UUFDVixJQUFJLEVBQUUsb0NBQVMsQ0FBQyxNQUFNO1FBQ3RCLFVBQVUsRUFBRTtZQUNSLGFBQWE7WUFDYixJQUFJLEVBQUU7Z0JBQ0osdUJBQXVCO2dCQUN2QixJQUFJLEVBQUUsb0NBQVMsQ0FBQyxNQUFNO2dCQUN0Qix5QkFBeUI7Z0JBQ3pCLEtBQUssRUFBRSxNQUFNO2FBQ2Q7U0FDSjtLQUNGO0NBQ0YsQ0FBQyxDQUFDO0FBRUgsa0JBQWUsa0NBQU8sQ0FBQyJ9