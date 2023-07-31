import { basekit, Component, ParamType } from '@lark-opdev/block-basekit-server-api';

basekit.addAction({
  formItems: [
    {
      itemId: 'text',
      label: '源文本',
      required: true,
      component: Component.Input,
      componentProps: {
        mode: 'textarea',
        placeholder: '请输入源文本或选择引用列',
      }
    },
    {
      itemId: 'transformType',
      label: '转换类型',
      required: true,
      component: Component.SingleSelect,
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
  execute: async function(args, context) {
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
    type: ParamType.Object,
    properties: {
        // 声明 text 属性
        text: {
          // 声明 text 字段类型为 string
          type: ParamType.String,
          // 声明在节点 UI 上展示的文案为「转换结果」
          label: '转换结果',
        },
    }
  }
});

export default basekit;
