{
  description = "Open SmartCity Home development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    # Tools
    flake-parts.url = "github:hercules-ci/flake-parts";
    flake-root.url = "github:srid/flake-root";

    process-compose-flake.url = "github:Platonic-Systems/process-compose-flake";
  };

  outputs = inputs@{ flake-parts, nixpkgs, ... }:
    flake-parts.lib.mkFlake { inherit inputs; } {
      systems = [ "aarch64-darwin" "x86_64-linux" "aarch64-linux" ];

      imports = [
        inputs.flake-root.flakeModule
        inputs.process-compose-flake.flakeModule
      ];

      perSystem = { self', pkgs, system, lib, config, ... }:
        let
          graalvm-ce = pkgs.graalvm-ce;
          kotlin = pkgs.kotlin.override { jre = pkgs.graalvm-ce; };

          inherit (pkgs.stdenv) isDarwin;
        in
        {
          _module.args.pkgs = import nixpkgs {
            inherit system;
            config.allowUnfree = true;
          };
          devShells.default = pkgs.mkShell {
            nativeBuildInputs = with pkgs;
              [
                graalvm-ce
                kotlin
              ] ++ lib.optionals isDarwin (with pkgs.darwin.apple_sdk.frameworks; [
                CoreFoundation
                CoreServices
                pkgs.xcbuild
              ]);

            packages = with pkgs; [
              gradle
              just
              jdt-language-server
              self'.packages.open-smart-city-home-dev
            ];

             inputsFrom = [
               config.flake-root.devShell
             ];
          };

          process-compose.open-smart-city-home-dev = {
              cli.options.port = 8081;
              settings = {
                environment = {

                };

                processes = {
                  app = {
                    command = "./gradlew run";
                  };
                };
              };
            };
        };
    };
}
