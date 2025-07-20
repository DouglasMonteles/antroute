export const randNumber: (distance?: number) => number = (distance: number = 500) => {
  return Math.random() * distance;
}