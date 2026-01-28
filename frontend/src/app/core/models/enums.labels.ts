import { EEntityType } from './enums';
import { EComponentType } from './enums';

export const EEntityTypeLabelMap: Record<EEntityType, string> = {
  [EEntityType.CGF]: 'Cgf',
  [EEntityType.OWNSHIP]: 'Ownship'
};

export const EComponentTypeLabelMap: Record<EComponentType, string> = {
  [EComponentType.SelectComponent]: 'Select',
  [EComponentType.ToggleComponent]: 'Toggle',
  [EComponentType.IntegerInputComponent]: 'IntegerInput',
  [EComponentType.FloatInputComponent]: 'FloatInput',
  [EComponentType.PositionComponent]: 'Position',
  [EComponentType.PositionWithHeightComponent]: 'PositionWithHeight',
  [EComponentType.RadioGroupComponent]: 'RadioGroup',
  [EComponentType.SliderComponent]: 'Slider',
  [EComponentType.SwitchComponent]: 'Switch',
  [EComponentType.SegmentedSelectionComponent]: 'SegmentedSelection',
  [EComponentType.TextInputComponent]: 'TextInput',
  [EComponentType.TimeComponent]: 'Time',
  [EComponentType.HiddenComponent]: 'Hidden',
  [EComponentType.DirectionComponent]: 'Direction',
  [EComponentType.BuildingComponent]: 'Building'
};