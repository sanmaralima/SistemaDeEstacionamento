interface Env {
  readonly NG_APP_API_URL?: string;
  readonly [key: string]: string | undefined;
}

interface Process {
  readonly env: Env;
}

declare var process: Process;
