import {getSelected, getDays} from '../src/DayPicker';


describe('DayPicker tests', function () {

  it('should compute selected from empty days', function () {
    const days = [];
    const selected = getSelected(days);
    expect(selected).toEqual([false, false, false, false, false, false, false]);
  });

  it('should compute selected days', function () {
    const days = [0, 3];
    const selected = getSelected(days);
    expect(selected).toEqual([true, false, false, true, false, false, false]);
  });

  it('should compute days', function () {
    const selected = [true, false, true, false, false, false, false];
    const days = getDays(selected);
    expect(days).toEqual([0,2]);
  });

});
