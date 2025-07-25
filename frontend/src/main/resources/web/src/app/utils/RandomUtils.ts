export const randNumber: (distance?: number) => number = (distance: number = 500) => {
  return Math.random() * distance;
}

export const randHexColor = () => {
  const randomHex = Math.floor(Math.random() * 16777215).toString(16);
  return `#${randomHex.padStart(6, '0')}`;
}