/**
 * 把数据库里的月龄（单位：月）转成更好读的文案。
 * 小于 12 个月显示「X 个月」，满 12 个月显示「X 岁」，有零头时带上「Y 个月」。
 */
export function ageText(age) {
  const months = Number(age);
  if (age === null || age === undefined || Number.isNaN(months) || months < 0) {
    return '月龄未知';
  }
  if (months < 12) {
    return months + '个月';
  }
  const years = Math.floor(months / 12);
  const rest = months % 12;
  return rest ? years + '岁' + rest + '个月' : years + '岁';
}
