export type ApiErrorKind = 'validation' | 'auth' | 'forbidden' | 'not-found' | 'network' | 'server' | 'unknown';

export interface ApiError {
  kind: ApiErrorKind;
  status: number;
  message: string;
  details?: string;
}
