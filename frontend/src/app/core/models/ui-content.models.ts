import { EUiContentType } from './enums';

export interface UiContentDTO {
  name: string;
  cssClassName: string;
  type: EUiContentType;
  itemIdList: number[];
}

export interface UiContent extends UiContentDTO {}
