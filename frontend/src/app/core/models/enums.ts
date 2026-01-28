export enum EEntityType {
  CGF = 'CGF',
  OWNSHIP = 'OWNSHIP'
}

export enum EComponentType {
    SelectComponent = 'SelectComponent',
    ToggleComponent = 'ToggleComponent',
    IntegerInputComponent = 'IntegerInputComponent',
    FloatInputComponent = 'FloatInputComponent',
    PositionComponent = 'PositionComponent',
    PositionWithHeightComponent = 'PositionWithHeightComponent',
    RadioGroupComponent = 'RadioGroupComponent',
    SliderComponent = 'SliderComponent',
    SwitchComponent = 'SwitchComponent',
    SegmentedSelectionComponent = 'SegmentedSelectionComponent',
    TextInputComponent = 'TextInputComponent',
    TimeComponent = 'TimeComponent',
    HiddenComponent = 'HiddenComponent',
    DirectionComponent = 'DirectionComponent',
    BuildingComponent = 'BuildingComponent'
}

export enum EUiContentType {
    DEFAULT= 'DEFAULT',
  CONTROL_DRIVING = 'CONTROL_DRIVING',
  ROUTE_POINT_RULE_PROPERTIES = 'ROUTE_POINT_RULE_PROPERTIES',
  AREA_RULE_PROPERTIES = 'AREA_RULE_PROPERTIES',
  GROUP = 'GROUP'
}

export enum EPropertyItemType {
  DEFAULT = 'DEFAULT',
  LEADER_AND_MEMBER_ONLY = 'LEADER_AND_MEMBER_ONLY',
  LEADER_ONLY = 'LEADER_ONLY'
}
