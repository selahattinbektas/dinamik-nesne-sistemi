import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'uiContentTypeLabel'
})
export class UiContentTypeLabelPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) {
      return '';
    }

    return value
      .split('_')
      .filter((segment) => segment.length > 0)
      .map((segment) => {
        const lowerCased = segment.toLowerCase();
        return `${lowerCased.charAt(0).toUpperCase()}${lowerCased.slice(1)}`;
      })
      .join(' ');
  }
}