import React from 'react';
import {
  AvatarGroup,
  Avatar,
  Typography,
  Checkbox,
  Select,
  DatePicker,
} from '@douyinfe/semi-ui';
import {
  FieldType,
  SelectFieldMeta,
  IOpenSegment,
  IOpenUser,
  IOpenSegmentType,
  IOpenMultiSelect,
  IWidgetTable,
  FieldMeta,
  IOpenSingleSelect,
  ISelectFieldOption,
} from '@lark-opdev/block-bitable-api';
const { Text } = Typography;
const colors = [
  'amber',
  'blue',
  'cyan',
  'green',
  'grey',
  'indigo',
  'light-blue',
  'light-green',
  'lime',
  'orange',
  'pink',
  'purple',
  'red',
  'teal',
  'violet',
  'yellow',
];

interface IRenderFuncContext {
  table: IWidgetTable;
  meta: FieldMeta;
}

function colorHelper(name?: string) {
  return colors[(name?.codePointAt(0) || 0) % colors.length];
}
// 渲染文本类型方法
function renderSegment(context: IRenderFuncContext) {
  return (segs: IOpenSegment[]) => {
    return (
      <>
        {(segs || []).map((segs) => (
          <Text
            link={
              segs.type === IOpenSegmentType.Url
                ? { href: segs.link, target: '_blank' }
                : void 0
            }
          >
            {segs.text}
          </Text>
        ))}
      </>
    );
  };
}
// 渲染人员类型方法
function renderUser(context: IRenderFuncContext) {
  return (users: IOpenUser[]) => {
    return (
      <AvatarGroup size="small">
        {(users || []).map((user) => (
          <Avatar alt={user.name} color={colorHelper(user.name) as any}>
            {user.name}
          </Avatar>
        ))}
      </AvatarGroup>
    );
  };
}
// 渲染复选框方法
function renderCheckBox(context: IRenderFuncContext) {
  return (checked: boolean, record: { recordId: string }) => {
    return (
      <Checkbox
        defaultChecked={!!checked}
        onChange={(checked) => {
          if (!record || !record.recordId) return;
          // bitable 设值接口
          context.table.setCellValue(
            context.meta.id,
            record.recordId,
            !!checked.target.checked
          );
        }}
      ></Checkbox>
    );
  };
}
// 渲染单选 / 多选
function renderOption(context: IRenderFuncContext) {
  const { table, meta } = context;
  const {
    id,
    type,
    property: { options },
  } = meta as SelectFieldMeta;
  const optionMap = new Map<string, ISelectFieldOption>();
  options.forEach((option) => optionMap.set(option.id, option));
  return (
    option: IOpenMultiSelect | IOpenSingleSelect,
    record: { recordId: string }
  ) => {
    const multiple = FieldType.MultiSelect === type;
    option = option
      ? ((Array.isArray(option) ? option : [option]) as IOpenMultiSelect)
      : [];
    return (
      <>
        <Select
          style={{ width: '150px' }}
          multiple={multiple}
          defaultValue={option.map((op) => (multiple ? op.id : op.text))}
          onChange={(value) => {
            value = Array.isArray(value) ? value : [value];
            const cellValues = (value as string[])
              .map((id) => optionMap.get(id)!)
              .map((option) => ({
                id: option.id,
                text: option.name,
              }));

            table.setCellValue<IOpenSingleSelect | IOpenMultiSelect>(
              id,
              record.recordId,
              multiple ? cellValues : cellValues[0]
            );
          }}
        >
          {options.map((option) => (
            <Select.Option value={option.id}>{option.name}</Select.Option>
          ))}
        </Select>
      </>
    );
  };
}
// 渲染时间选择器
function renderDate(context: IRenderFuncContext) {
  return (time: number, record: { recordId: string }) => {
    return (
      <DatePicker
        defaultValue={time}
        disabled={context.meta.type !== FieldType.DateTime}
        onChange={(date) => {
          const dateString = date?.toLocaleString() || '';
          if (!dateString) return;
          const time = new Date(dateString).getTime();

          context.table.setCellValue(context.meta.id, record.recordId, time);
        }}
      />
    );
  };
}

// 渲染方法 map
const RenderFuncMap: Partial<
  Record<
    FieldType,
    (context: IRenderFuncContext) => (...args: any[]) => React.ReactNode
  >
> = {
  [FieldType.User]: renderUser,
  [FieldType.CreatedUser]: renderUser,
  [FieldType.ModifiedUser]: renderUser,
  [FieldType.Text]: renderSegment,
  [FieldType.Url]: renderSegment,
  [FieldType.Checkbox]: renderCheckBox,
  [FieldType.SingleSelect]: renderOption,
  [FieldType.MultiSelect]: renderOption,
  [FieldType.DateTime]: renderDate,
  [FieldType.CreatedTime]: renderDate,
  [FieldType.ModifiedTime]: renderDate,
};
// default 渲染方法
const renderDefault =
  () =>
  (...args: unknown[]) =>
    <>{args[0]}</>;

export function getRenderFunc(context: IRenderFuncContext) {
  const { meta } = context;
  const render = RenderFuncMap[meta.type] || renderDefault;
  return render(context) as (...args: any[]) => React.ReactNode;
}
